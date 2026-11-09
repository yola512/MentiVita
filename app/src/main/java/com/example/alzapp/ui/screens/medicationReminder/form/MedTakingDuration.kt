package com.example.alzapp.ui.screens.medicationReminder.form

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alzapp.utils.AppConfig.DATE_FORMATTER_1
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun MedTakingDuration(
    isReadOnly: Boolean,
    startDate: LocalDate,
    endDate: LocalDate,
    onStartDateSelected: (LocalDate) -> Unit,
    onEndDateSelected: (LocalDate) -> Unit
) {
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    if (showStartPicker) {
        DatePickerModal(
            initialDate = startDate,
            onDateSelected = { millis ->
                millis?.let {
                    val date = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                    onStartDateSelected(date)
                }
                showStartPicker = false
            },
            onDismiss = { showStartPicker = false }
        )
    }

    if (showEndPicker) {
        DatePickerModal(
            initialDate = endDate,
            onDateSelected = { millis ->
                millis?.let {
                    val date = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                    onEndDateSelected(date)
                }
                showEndPicker = false
            },
            onDismiss = { showEndPicker = false }
        )
    }

    Column {
        Text(
            text = "Od kiedy do kiedy brać lek?",
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleMedium,
            lineHeight = 30.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(50.dp)
                ),
            shape = RoundedCornerShape(50.dp),
            color = MaterialTheme.colorScheme.onPrimary
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 6.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(enabled = !isReadOnly) { showStartPicker = true }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center) {
                    Text(
                        text = startDate.format(DATE_FORMATTER_1),
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        softWrap = false
                    )
                }
                Text(
                    text = "-",
                    style = MaterialTheme.typography.bodyLarge
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(enabled = !isReadOnly) { showEndPicker = true }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center)
                {
                    Text(
                        text = endDate.format(DATE_FORMATTER_1),
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }
    }
}