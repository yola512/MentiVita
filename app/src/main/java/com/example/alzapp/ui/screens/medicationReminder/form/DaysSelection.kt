package com.example.alzapp.ui.screens.medicationReminder.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DaysSelection(
    isReadOnly: Boolean,
    startDate: LocalDate,
    endDate: LocalDate,
    selectedDays: Set<String>,
    onDaysSelectedChange: (Set<String>) -> Unit
) {
    val days = listOf(
        "Codziennie",
        "Poniedziałek",
        "Wtorek",
        "Środa",
        "Czwartek",
        "Piątek",
        "Sobota",
        "Niedziela"
    )

    val isOneDay = startDate == endDate

    Column {
        Text(
            text = "Dni przyjmowania leku",
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (isOneDay) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Lek wybrany tylko na jeden dzień: ${
                        startDate.format(
                            DateTimeFormatter.ofPattern(
                                "dd.MM.yyyy"
                            )
                        )
                    }",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(16.dp),
                    lineHeight = 40.sp,
                    textAlign = TextAlign.Start
                )
            }
        } else {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                days.forEach { day ->
                    val isSelected = selectedDays.contains(day)
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (isSelected) Color.White else Color.Black,
                        modifier = Modifier.clickable(enabled = !isReadOnly) {
                            val newSelectedDays = if (day == "Codziennie") {
                                setOf("Codziennie")
                            } else {
                                val mutableDays = selectedDays.toMutableSet()
                                mutableDays.remove("Codziennie")
                                if (isSelected) {
                                    mutableDays.remove(day)
                                    if (mutableDays.isEmpty()) setOf("Codziennie") else mutableDays
                                } else {
                                    mutableDays.add(day)
                                    mutableDays
                                }
                            }
                            onDaysSelectedChange(newSelectedDays)
                        }
                    ) {
                        Text(
                            text = day,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
                        )
                    }
                }
            }
        }
    }
}