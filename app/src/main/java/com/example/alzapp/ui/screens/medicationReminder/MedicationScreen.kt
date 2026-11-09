package com.example.alzapp.ui.screens.medicationReminder

import com.example.alzapp.ui.components.CalendarSlider
import com.example.alzapp.ui.screens.medicationReminder.viewModel.MedicationViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alzapp.R
import com.example.alzapp.data.medicationReminder.local.MedicationEntity
import com.example.alzapp.ui.screens.medicationReminder.viewModel.MedicationDose
import com.example.alzapp.ui.screens.medicationReminder.viewModel.MedicationStatus
import com.example.alzapp.ui.screens.medicationReminder.viewModel.TimeOfDay
import com.example.alzapp.utils.AppConfig
import com.example.alzapp.utils.AppConfig.TIME_FORMATTER
import com.example.alzapp.utils.declineDose
import java.time.LocalDate
import java.time.LocalTime

@Composable
internal fun MedicationScreen(
    viewModel: MedicationViewModel,
    onAddMedicationClick: (LocalDate) -> Unit,
    onMedicationClick: (MedicationEntity) -> Unit
) {
    val groupedMedications by viewModel.groupedMedications.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val highlightedMedications by viewModel.highlightedMedications.collectAsState()
    val listState = rememberLazyListState()

    MedicationScreenContent(
        groupedMedications = groupedMedications,
        selectedDate = selectedDate,
        highlightedMedications = highlightedMedications,
        listState = listState,
        getMedicationStatus = { med, date, time -> viewModel.getMedicationStatus(med, date, time) },
        onDateSelected = { viewModel.onDateSelected(it) },
        onAddClick = { onAddMedicationClick(selectedDate) },
        onMedicationClick = { 
            viewModel.clearHighlights()
            onMedicationClick(it) 
        },
        onTakenClick = { clickedMedication, time ->
            viewModel.toggleMedicationTaken(clickedMedication, selectedDate, time)
        }
    )
}

@Composable
private fun MedicationScreenContent(
    groupedMedications: Map<TimeOfDay, List<MedicationDose>>,
    selectedDate: LocalDate = LocalDate.now(),
    highlightedMedications: Set<Pair<Int, LocalTime>> = emptySet(),
    listState: LazyListState = rememberLazyListState(),
    getMedicationStatus: (MedicationEntity, LocalDate, LocalTime) -> MedicationStatus,
    onDateSelected: (LocalDate) -> Unit = {},
    onAddClick: () -> Unit = {},
    onMedicationClick: (MedicationEntity) -> Unit = {},
    onTakenClick: (MedicationEntity, LocalTime) -> Unit
) {
    // collect all elements (headers and doses) into a flat list to find indices for scrolling
    val flatList = remember(groupedMedications) {
        val list = mutableListOf<Any>()
        groupedMedications.forEach { (timeOfDay, doses) ->
            list.add(timeOfDay)
            doses.forEach { list.add(it) }
        }
        list
    }

    // fixed scrolling: find the item that is EARLIEST on the UI list among all highlights
    LaunchedEffect(highlightedMedications, flatList) {
        if (highlightedMedications.isNotEmpty()) {
            val indexInFlatList = flatList.indexOfFirst { item ->
                item is MedicationDose && 
                highlightedMedications.contains(item.medication.id to item.time)
            }
            
            if (indexInFlatList != -1) {
                // adjust index: +4 because of the 4 items added before the list (Calendar, TodayButton, Text, Button)
                listState.animateScrollToItem(indexInFlatList + 4)
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                CalendarSlider(
                    selectedDate = selectedDate,
                    onDateSelected = onDateSelected,
                    locale = AppConfig.PL_LOCALE
                )
            }
            item {
                if (selectedDate != LocalDate.now()) {
                    Button(
                        onClick = { onDateSelected(LocalDate.now()) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(top = 12.dp)
                    ) {
                        Text(text = "WRÓĆ DO DZISIAJ", color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
                    }
                } else {
                   Spacer(modifier = Modifier.height(16.dp))
                }
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "LEKI",
                    fontWeight = FontWeight.W900,
                    style = MaterialTheme.typography.displaySmall,
                    letterSpacing = 2.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = selectedDate.format(AppConfig.DATE_FORMATTER).replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(25.dp))
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = onAddClick,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                        shape = RoundedCornerShape(32.dp),
                        elevation = ButtonDefaults.buttonElevation(8.dp),
                        modifier = Modifier.height(64.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icon_add),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(
                            text = "DODAJ LEK",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (groupedMedications.isEmpty()) {
                item {
                    Text(
                        text = "Brak leków zaplanowanych na ten dzień.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
                groupedMedications.forEach { (timeOfDay, doses) ->
                    item(key = "header_${timeOfDay.name}") {
                        TimeOfDayHeader(timeOfDay)
                    }
                    itemsIndexed(doses, key = { _, dose -> dose.medication.id.toString() + "_" + dose.time.toString() }) { _, dose ->
                        val status = getMedicationStatus(dose.medication, selectedDate, dose.time)
                        val isTaken = status is MedicationStatus.AlreadyTaken
                        val shouldHighlight = highlightedMedications.contains(dose.medication.id to dose.time) && 
                                            status is MedicationStatus.CanTake
                        
                        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                            MedicationItem(
                                medicationName = dose.medication.name,
                                dose = declineDose(dose.medication.dose),
                                takingTime = dose.time.format(TIME_FORMATTER),
                                isTaken = isTaken,
                                isHighlighted = shouldHighlight,
                                status = status,
                                onTakeClick = { onTakenClick(dose.medication, dose.time) },
                                onMedicationClick = { onMedicationClick(dose.medication) }
                            )
                        }
                    }
                }
            }
        }
    }
}
