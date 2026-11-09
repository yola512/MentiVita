package com.example.alzapp.ui.screens.medicationReminder.form

import com.example.alzapp.ui.components.NameField
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alzapp.R
import com.example.alzapp.data.medicationReminder.local.MedicationEntity
import com.example.alzapp.utils.FormMode
import com.example.alzapp.ui.components.CustomBackButton
import com.example.alzapp.ui.components.DeletePopUp
import com.example.alzapp.ui.components.InfoDialog
import com.example.alzapp.ui.screens.medicationReminder.viewModel.MedicationViewModel
import com.example.alzapp.ui.components.ActionButtons
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import com.example.alzapp.utils.AppConfig.TIME_FORMATTER

@Composable
internal fun MedicationForm(
    viewModel: MedicationViewModel,
    mode: FormMode,
    medicationId: Int? = null,
    initialDate: String? = null,
    onBackClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
) {
    // better ux; keyboard hides when user clicks on the background
    val focusManager = LocalFocusManager.current

    val passedDate = initialDate?.let { LocalDate.parse(it) } ?: LocalDate.now()

    // form state holding user input
    var medicationName by remember { mutableStateOf("") }
    var selectedNumber by remember { mutableStateOf("1") }
    var selectedUnit by remember { mutableStateOf("tabletek") }
    var startDate by remember { mutableStateOf(passedDate) }
    var endDate by remember { mutableStateOf(passedDate.plusMonths(1)) }
    var selectedTimes by remember { mutableStateOf(emptyList<LocalTime>()) }
    var selectedDays by remember { mutableStateOf(setOf("Codziennie")) }

    var currentMedication by remember { mutableStateOf<MedicationEntity?>(null) }
    var showDeleteDialogForMed by remember { mutableStateOf(false) }
    var showDeleteDialogForTime by remember { mutableStateOf(false) }
    var timeToDelete by remember { mutableStateOf<LocalTime?>(null) }
    var showInfoDialog by remember { mutableStateOf(false) }

    val isReadOnly = mode == FormMode.INFO
    val formTitle = when (mode) {
        FormMode.ADD -> "DODAJ NOWY LEK"
        FormMode.INFO -> "INFORMACJA O LEKU"
        FormMode.EDIT -> "EDYTUJ LEK"
    }

    // load data if in edit/info mode
    LaunchedEffect(medicationId) {
        if (medicationId != null) {
            val med = viewModel.getMedicationById(medicationId)
            if (med != null) {
                currentMedication = med
                medicationName = med.name
                val doseParts = med.dose.split(" ")
                if (doseParts.size >= 2) {
                    selectedNumber = doseParts[0]
                    selectedUnit = doseParts[1]
                }
                startDate = med.startDate
                endDate = med.endDate
                if (med.takingTimes.isNotEmpty()) {
                    selectedTimes = med.takingTimes.map { it.toLocalTime() }
                }
                selectedDays = med.takingDays
            }
        }
    }

    // delete pop up to ensure user wants to delete the medication
    if (showDeleteDialogForMed) {
        DeletePopUp(
            headlineFinish = "ten lek",
            entity = "$medicationName, $selectedNumber $selectedUnit",
            onDeleteClick = {
                currentMedication?.let {
                    viewModel.deleteMedication(it)
                    onBackClick()
                }
                showDeleteDialogForMed = false
            },
            onDiscardClick = { showDeleteDialogForMed = false }
        )
    }
    // delete pop up to ensure user wants to delete time related to medication
    if (showDeleteDialogForTime) {
        DeletePopUp(
            headlineFinish = "tę godzinę",
            entity = timeToDelete?.format(TIME_FORMATTER) ?: "",
            onDeleteClick = {
                selectedTimes = selectedTimes.filter { it != timeToDelete }
                showDeleteDialogForTime = false
                timeToDelete = null
            },
            onDiscardClick = { 
                showDeleteDialogForTime = false
                timeToDelete = null
            }
        )
    }

    // tips on how to fill out the form
    if (showInfoDialog) {
        InfoDialog(
            title = "Wskazówki",
            tips = listOf(
                "Dlaczego przycisk 'ZAPISZ' nie działa?\nAby dodać lek, wpisz przynajmniej jego nazwę, dawkę i wybierz godzinę. Bez tego przycisk „ZAPISZ” nie będzie działać.",
                "Różne dawki w ciągu dnia\nDodaj każdą porę jako nowy lek. (Przykład: dodaj osobno 2 tabletki rano i osobno 1 tabletkę wieczorem).",
                "Przypomnienia o lekach\nGdy zadzwoni alarm, oznacz lek jako wzięty. Jeśli tego nie zrobisz, przypomnimy Ci ponownie za 10 minut."
            ),
            onDismissRequest = { showInfoDialog = false }
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
            },
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                CustomBackButton(iconSize = 60.dp, onClick = onBackClick)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = formTitle,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground,
                        softWrap = true,
                        lineHeight = 40.sp,
                    )
                }
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(25.dp),
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    item {
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 10.dp),
                            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 5.dp),
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ){
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 33.dp, horizontal = 12.dp),
                                verticalArrangement = Arrangement.spacedBy(15.dp)
                            ) {
                                // medicine name
                                NameField(
                                    isReadOnly = isReadOnly,
                                    header = "Nazwa leku",
                                    placeholder = "tu wpisz nazwę leku",
                                    name = medicationName,
                                    onNameChange = { medicationName = it }
                                )
                                // dose + unit
                                DoseSpinner(
                                    isReadOnly = isReadOnly,
                                    selectedNumber = selectedNumber,
                                    onNumberChange = { selectedNumber = it },
                                    selectedUnit = selectedUnit,
                                    onUnitChange = { selectedUnit = it }
                                )
                                MedTakingDuration(
                                    isReadOnly = isReadOnly,
                                    startDate = startDate,
                                    endDate = endDate,
                                    onStartDateSelected = { 
                                        startDate = it
                                        if (startDate.isAfter(endDate)) {
                                            endDate = startDate
                                        }
                                    },
                                    onEndDateSelected = { 
                                        endDate = it
                                        if (endDate.isBefore(startDate)) {
                                            startDate = endDate
                                        }
                                    }
                                )
                                DaysSelection(
                                    isReadOnly = isReadOnly,
                                    startDate = startDate,
                                    endDate = endDate,
                                    selectedDays = selectedDays,
                                    onDaysSelectedChange = { selectedDays = it }
                                )
                                // medicine taking time picker
                                MedicineTimePicker(
                                    isReadOnly = isReadOnly,
                                    selectedTimes = selectedTimes,
                                    onTimesSelected = { selectedTimes = it },
                                    onDeleteRequest = { time ->
                                        timeToDelete = time
                                        showDeleteDialogForTime = true
                                    }
                                )

                                val isFormValid = medicationName.isNotBlank() && 
                                                 selectedNumber.isNotBlank() && 
                                                 selectedTimes.isNotEmpty()

                                ActionButtons(
                                    mode = mode,
                                    isSaveEnabled = isFormValid,
                                    onSaveClick = {
                                        val finalSelectedDays = if (startDate == endDate) setOf("Codziennie") else selectedDays
                                        val newMed = MedicationEntity(
                                            name = medicationName,
                                            dose = "$selectedNumber $selectedUnit",
                                            startDate = startDate,
                                            endDate = endDate,
                                            takingTimes = selectedTimes.map {
                                                LocalDateTime.of(
                                                    passedDate,
                                                    it
                                                )
                                            },
                                            takingDays = finalSelectedDays,
                                            addedDate = LocalDateTime.of(passedDate, LocalTime.MIDNIGHT)
                                        )
                                        viewModel.addMedication(newMed)
                                        onBackClick()
                                    },
                                    onUpdateClick = {
                                        val finalSelectedDays = if (startDate == endDate) setOf("Codziennie") else selectedDays
                                        currentMedication?.let { med ->
                                            val updatedMed = med.copy(
                                                name = medicationName,
                                                dose = "$selectedNumber $selectedUnit",
                                                startDate = startDate,
                                                endDate = endDate,
                                                takingTimes = selectedTimes.map {
                                                    LocalDateTime.of(
                                                        med.addedDate.toLocalDate(),
                                                        it
                                                    )
                                                },
                                                takingDays = finalSelectedDays
                                            )
                                            viewModel.updateMedication(updatedMed)
                                            onBackClick()
                                        }
                                    },
                                    onDeleteClick = {
                                        showDeleteDialogForMed = true
                                    },
                                    onDiscardClick = { onBackClick() },
                                    onEditClick = onEditClick
                                )
                            }
                        }
                    }
                }
            }

            // tips button positioned absolutely to avoid being pushed by CustomBackButton's internal row
            IconButton(
                onClick = { showInfoDialog = true },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(60.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.icon_info),
                    contentDescription = "Show info",
                    modifier = Modifier.size(50.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
