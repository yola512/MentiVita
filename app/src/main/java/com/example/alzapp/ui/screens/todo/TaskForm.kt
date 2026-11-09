package com.example.alzapp.ui.screens.todo

import com.example.alzapp.ui.components.NameField
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTimePickerState
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
import com.example.alzapp.data.todo.local.TodoTaskEntity
import com.example.alzapp.ui.components.TimePickerDialog
import com.example.alzapp.ui.components.CustomBackButton
import com.example.alzapp.utils.FormMode
import com.example.alzapp.ui.components.DeletePopUp
import com.example.alzapp.ui.screens.todo.viewModel.TodoViewModel
import com.example.alzapp.ui.components.ActionButtons
import com.example.alzapp.utils.AppConfig.TIME_FORMATTER
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
internal fun TaskForm (
    viewModel: TodoViewModel,
    mode: FormMode,
    taskId: Int? = null,
    initialDate: String? = null,
    onBackClick: () -> Unit = {},
    onEditClick: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current

    // states for form fields
    val passedDate = initialDate?.let { LocalDate.parse(it) } ?: LocalDate.now()
    var taskName by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }
    var reminderMinutes by remember { mutableStateOf<Int?>(null) }

    var currentTask by remember { mutableStateOf<TodoTaskEntity?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showDeleteTimeDialog by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    var isSaving by remember { mutableStateOf(false) }

    val isReadOnly = mode == FormMode.INFO
    val formTitle = when (mode) {
        FormMode.ADD -> "DODAJ NOWE ZADANIE"
        FormMode.INFO -> "INFORMACJA O ZADANIU"
        FormMode.EDIT -> "EDYTUJ ZADANIE"
    }

    // load task data from database if we have task id (editing/viewing info)
    LaunchedEffect(taskId) {
        if (taskId != null) {
            val task = viewModel.getTaskById(taskId)
            if (task != null) {
                currentTask = task
                taskName = task.title
                taskDescription = task.taskDescription
                selectedTime = task.time
                reminderMinutes = task.reminderMinutesBefore
            }
        }
    }

    // dialog pop-up to make sure if the user really wants to delete the task
    if (showDeleteDialog) {
        DeletePopUp(
            headlineFinish = "to zadanie",
            entity = if (taskDescription.isNotEmpty()) "$taskName\n$taskDescription" else taskName,
            onDeleteClick = {
                currentTask?.let {
                    viewModel.deleteTodo(it)
                    onBackClick()
                }
                showDeleteDialog = false
            },
            onDiscardClick = { showDeleteDialog = false }
        )
    }

    // dialog pop-up to make sure if the user really wants to delete the time related to the task
    if (showDeleteTimeDialog) {
        DeletePopUp(
            headlineFinish = "tę godzinę",
            entity = selectedTime?.format(TIME_FORMATTER) ?: "",
            onDeleteClick = {
                selectedTime = null
                reminderMinutes = null
                showDeleteTimeDialog = false
            },
            onDiscardClick = { showDeleteTimeDialog = false }
        )
    }

    // time picker
    val timePickerState = rememberTimePickerState(
        initialHour = selectedTime?.hour ?: 12,
        initialMinute = selectedTime?.minute ?: 0,
        is24Hour = true
    )

    if (showTimePicker) {
        TimePickerDialog(
            timePickerState = timePickerState,
            onDismiss = { showTimePicker = false },
            onConfirm = {
                selectedTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                showTimePicker = false
            }
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                // click on the background cancels focus e.g. on text fields
                focusManager.clearFocus()
            },
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            CustomBackButton(iconSize = 60.dp, onClick = onBackClick)
            // screen title
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
                            .fillMaxHeight()
                            .padding(horizontal = 10.dp, vertical = 10.dp),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 5.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                    {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 33.dp, horizontal = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(15.dp)
                        ) {
                            // task name input
                            NameField(
                                isReadOnly = isReadOnly, 
                                header = "Nazwa zadania", 
                                name = taskName,
                                placeholder = "tu wpisz nazwę zadania",
                                onNameChange = { taskName = it }
                            )
                            // task description input
                            NameField(
                                isReadOnly = isReadOnly,
                                header = "Opis zadania",
                                name = taskDescription,
                                placeholder = "tu wpisz opis zadania",
                                onNameChange = { taskDescription = it }
                            )

                            // time selection
                            Column {
                                Text(
                                    text = "Godzina (opcjonalnie)",
                                    fontWeight = FontWeight.SemiBold,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = RoundedCornerShape(50.dp),
                                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.5f)),
                                        modifier = Modifier
                                            .clickable(enabled = !isReadOnly)
                                            {
                                                showTimePicker = true
                                            }
                                            .border(
                                                width = 2.dp,
                                                color = MaterialTheme.colorScheme.surfaceVariant,
                                                shape = RoundedCornerShape(50.dp)
                                            ),
                                        color = MaterialTheme.colorScheme.surface
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = ImageVector.vectorResource(R.drawable.icon_clock),
                                                contentDescription = null,
                                                modifier = Modifier.size(30.dp)
                                            )
                                            Text(
                                                text = selectedTime?.format(TIME_FORMATTER) ?: "Całodniowe",
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                        }
                                    }
                                    // btn to delete time if it was selected
                                    if (selectedTime != null && !isReadOnly) {
                                        IconButton(
                                            onClick = { showDeleteTimeDialog = true },
                                            modifier = Modifier.size(60.dp)
                                        ) {
                                            Icon(
                                                imageVector = ImageVector.vectorResource(R.drawable.icon_delete),
                                                contentDescription = "Usuń godzinę",
                                                modifier = Modifier.size(48.dp),
                                                tint = MaterialTheme.colorScheme.error
                                            )
                                        }
                                    }
                                }
                            }

                            // reminder options (only shown if time was selected)
                            if (selectedTime != null) {
                                Column {
                                    Text(
                                        text = "Przypomnienie",
                                        fontWeight = FontWeight.SemiBold,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    FlowRow(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        val timeStr = selectedTime?.format(TIME_FORMATTER) ?: ""
                                        val options = listOf(
                                            null to "Brak",
                                            0 to if (timeStr.isNotEmpty()) "Punktualnie o $timeStr" else "Punktualnie",
                                            15 to "15 min przed",
                                            30 to "30 min przed"
                                        )
                                        options.forEach { (mins, label) ->
                                            val isSelected = reminderMinutes == mins
                                            Surface(
                                                shape = RoundedCornerShape(50.dp),
                                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceDim,
                                                contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onTertiary,
                                                modifier = Modifier.clickable(enabled = !isReadOnly) {
                                                    reminderMinutes = mins
                                                }
                                            ) {
                                                Text(
                                                    text = label,
                                                    fontWeight = FontWeight.SemiBold,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // save, update, delete btns
                            ActionButtons(
                                mode = mode,
                                onSaveClick = {
                                    if (!isSaving) { // check if task is already saving
                                        isSaving = true // block so-called double taps (don't save the same task twice bc of double tap)
                                        val newTask = TodoTaskEntity(
                                            title = taskName,
                                            taskDescription = taskDescription,
                                            date = LocalDateTime.of(passedDate, LocalTime.MIDNIGHT),
                                            time = selectedTime,
                                            reminderMinutesBefore = reminderMinutes
                                        )
                                        viewModel.addTodo(newTask)
                                        onBackClick()
                                    }
                                },
                                onUpdateClick = {
                                    currentTask?.let {
                                        val updatedTask = it.copy(
                                            title = taskName,
                                            taskDescription = taskDescription,
                                            time = selectedTime,
                                            reminderMinutesBefore = reminderMinutes
                                        )
                                        viewModel.updateTodo(updatedTask)
                                        onBackClick()
                                    }
                                },
                                onDeleteClick = {
                                    showDeleteDialog = true
                                },
                                onDiscardClick = { onBackClick() },
                                onEditClick = onEditClick
                            )
                        }
                    }
                }
            }
        }
    }
}
