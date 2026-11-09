package com.example.alzapp.ui.screens.medicationReminder.form

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.alzapp.R
import com.example.alzapp.ui.components.TimePickerDialog
import com.example.alzapp.utils.AppConfig.TIME_FORMATTER
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MedicineTimePicker(
    isReadOnly: Boolean,
    selectedTimes: List<LocalTime>,
    onTimesSelected: (List<LocalTime>) -> Unit,
    onDeleteRequest: (LocalTime) -> Unit = {}
) {
    var showDialog by remember { mutableStateOf(false) }
    var editingIndex by remember { mutableStateOf<Int?>(null) }

    if (showDialog) {
        val initialTime = editingIndex?.let { selectedTimes.getOrNull(it) } ?: LocalTime.now()

        val timePickerState = rememberTimePickerState(
            initialHour = initialTime.hour,
            initialMinute = initialTime.minute,
            is24Hour = true
        )
        TimePickerDialog(
            timePickerState = timePickerState,
            onDismiss = {
                showDialog = false
                editingIndex = null
            },
            onConfirm = {
                val newTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                if (!selectedTimes.contains(newTime)) {
                    if (editingIndex != null && editingIndex!! < selectedTimes.size) {
                        val newList = selectedTimes.toMutableList()
                        newList[editingIndex!!] = newTime
                        onTimesSelected(newList.sorted())
                    } else {
                        onTimesSelected((selectedTimes + newTime).sorted())
                    }
                }
                showDialog = false
                editingIndex = null
            }
        )
    }

    Column {
        Text(
            text = "O której wziąć lek?",
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            selectedTimes.forEachIndexed { index, time ->
                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier
                        .clickable(enabled = !isReadOnly)
                        {
                            editingIndex = index
                            showDialog = true
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(
                            start = 16.dp,
                            top = 8.dp,
                            bottom = 8.dp,
                            end = if (!isReadOnly) 4.dp else 16.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.icon_clock),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                        Text(
                            text = time.format(TIME_FORMATTER),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        if (!isReadOnly) {
                            IconButton(
                                onClick = { onDeleteRequest(time) },
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.icon_delete),
                                    contentDescription = "Usuń godzinę",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                }
            }

            if (!isReadOnly) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.icon_add),
                    contentDescription = "Dodaj godzinę brania leku",
                    modifier = Modifier
                        .size(55.dp)
                        .clickable(onClick = {
                            editingIndex = null
                            showDialog = true
                        })
                )
            }
        }
    }
}