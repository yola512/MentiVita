package com.example.alzapp.ui.screens.medicationReminder.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alzapp.data.medicationReminder.local.MedicationEntity
import com.example.alzapp.data.medicationReminder.MedicationRepository
import com.example.alzapp.data.medicationReminder.MedicationScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

enum class TimeOfDay(val title: String, val order: Int) {
    MORNING("Rano", 1),
    AFTERNOON("Popołudnie", 2),
    EVENING("Wieczór", 3),
    NIGHT("Noc", 4)
}

data class MedicationDose(
    val medication: MedicationEntity,
    val time: LocalTime
)

sealed class MedicationStatus {
    object CanTake : MedicationStatus()
    data class TooEarly(val message: String) : MedicationStatus()
    data class TooLate(val message: String) : MedicationStatus()
    data class AlreadyTaken(val canUndo: Boolean) : MedicationStatus()
}

@HiltViewModel
internal class MedicationViewModel @Inject constructor(
    private val repository: MedicationRepository,
    private val scheduler: MedicationScheduler
) : ViewModel() {
    // date selected in calendar slider
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    // set of meds to visually highlight
    private val _highlightedMedications = MutableStateFlow<Set<Pair<Int, LocalTime>>>(emptySet())
    val highlightedMedications: StateFlow<Set<Pair<Int, LocalTime>>> = _highlightedMedications

    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
    }

    fun addHighlightedMedication(id: Int, time: LocalTime) {
        _highlightedMedications.value += (id to time)
    }

    fun clearHighlights() {
        _highlightedMedications.value = emptySet()
    }

    fun removeHighlight(id: Int, time: LocalTime) {
        _highlightedMedications.value -= (id to time)
    }

    // filters medications active on the selected date and day of week
    val medications: StateFlow<List<MedicationEntity>> = repository.allMedications
        .combine(_selectedDate) { meds, date ->
            meds.filter { med ->
                val isWithinDateRange = !date.isBefore(med.startDate) && !date.isAfter(med.endDate)
                val isTakingDay = med.takingDays.contains("Codziennie") ||
                                 med.takingDays.contains(dayOfWeekToPolish(date.dayOfWeek))

                isWithinDateRange && isTakingDay
            }.sortedByDescending { it.id }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // groups doses into time categories (Morning, Afternoon etc.)
    val groupedMedications: StateFlow<Map<TimeOfDay, List<MedicationDose>>> = medications
        .map { meds ->
            meds.flatMap { med ->
                med.takingTimes.map { dateTime ->
                    MedicationDose(med, dateTime.toLocalTime())
                }
            }
            .sortedBy { it.time }
            .groupBy { dose ->
                when (dose.time.hour) {
                    in 5..11 -> TimeOfDay.MORNING
                    in 12..17 -> TimeOfDay.AFTERNOON
                    in 18..23 -> TimeOfDay.EVENING
                    else -> TimeOfDay.NIGHT
                }
            }.toSortedMap(compareBy { it.order })
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    private fun dayOfWeekToPolish(day: DayOfWeek): String {
        return when (day) {
            DayOfWeek.MONDAY -> "Poniedziałek"
            DayOfWeek.TUESDAY -> "Wtorek"
            DayOfWeek.WEDNESDAY -> "Środa"
            DayOfWeek.THURSDAY -> "Czwartek"
            DayOfWeek.FRIDAY -> "Piątek"
            DayOfWeek.SATURDAY -> "Sobota"
            DayOfWeek.SUNDAY -> "Niedziela"
        }
    }

    // calculates if user can take med based on 1-hour time window
    fun getMedicationStatus(medication: MedicationEntity, date: LocalDate, time: LocalTime): MedicationStatus {
        val medicationDateTime = LocalDateTime.of(date, time)
        val now = LocalDateTime.now()
        
        // active window for pulsing and taking: from scheduled time up to 1 hour after
        val startWindow = medicationDateTime
        val endWindow = medicationDateTime.plusHours(1)
        val isWithinWindow = !now.isBefore(startWindow) && now.isBefore(endWindow)

        if (medication.takenDateTimes.contains(medicationDateTime)) {
            return MedicationStatus.AlreadyTaken(canUndo = isWithinWindow)
        }

        return when {
            now.isBefore(startWindow) -> {
                val timeOfDayMsg = when (time.hour) {
                    in 5..11 -> "Lek poranny"
                    in 12..17 -> "Lek popołudniowy"
                    in 18..23 -> "Lek wieczorny"
                    else -> "Lek na noc"
                }
                MedicationStatus.TooEarly("Poczekaj jeszcze trochę. $timeOfDayMsg")
            }
            now.isAfter(endWindow) -> {
                MedicationStatus.TooLate("Skonsultuj się z opiekunem, jeśli zapomniałeś tej dawki")
            }
            else -> MedicationStatus.CanTake
        }
    }

    // validates if current time is within the allowed taking window
    fun canMarkAsTaken(medicationDateTime: LocalDateTime): Boolean {
        val now = LocalDateTime.now()
        val endWindow = medicationDateTime.plusHours(1)
        return !now.isBefore(medicationDateTime) && now.isBefore(endWindow)
    }

    suspend fun getMedicationById(id: Int): MedicationEntity? {
        return repository.getMedicationById(id)
    }

    // saves new medication and schedules alarms
    fun addMedication(medication: MedicationEntity) = viewModelScope.launch {
        val id = repository.addMedication(medication)
        val savedMed = repository.getMedicationById(id.toInt())
        savedMed?.let { scheduler.scheduleMedication(it) }
    }

    // removes medication and cancels pending alarms
    fun deleteMedication(medication: MedicationEntity) = viewModelScope.launch {
        scheduler.cancelMedication(medication)
        repository.deleteMedication(medication)
    }

    // updates med details and refreshes alarm schedule
    fun updateMedication(medication: MedicationEntity) = viewModelScope.launch {
        val oldMedication = repository.getMedicationById(medication.id)
        if (oldMedication != null) {
            scheduler.cancelMedication(oldMedication)
        }
        repository.updateMedication(medication)
        scheduler.scheduleMedication(medication)
    }

    // handles marking med as taken or undoing action within allowed window
    fun toggleMedicationTaken(medication: MedicationEntity, date: LocalDate, time: LocalTime) = viewModelScope.launch {
        val dateTime = LocalDateTime.of(date, time)
        
        // block both marking and unmarking if outside the 1-hour window
        if (!canMarkAsTaken(dateTime)) return@launch

        val currentTakenDateTimes = medication.takenDateTimes.toMutableSet()
        
        if (currentTakenDateTimes.contains(dateTime)) {
            currentTakenDateTimes.remove(dateTime)
            // if unmarked, schedule a reminder for 10 minutes later
            scheduler.scheduleSnooze(medication.id, medication.name, medication.dose, time.toString())
        } else {
            currentTakenDateTimes.add(dateTime)
            // if user takes the medicine, cancel snooze alarms
            scheduler.cancelSnooze(medication.id, time.toString())
            
            // remove highlight for this specific medication/time after taking the med
            removeHighlight(medication.id, time)
        }

        val updatedMedication = medication.copy(takenDateTimes = currentTakenDateTimes)
        repository.updateMedication(updatedMedication)
    }
}
