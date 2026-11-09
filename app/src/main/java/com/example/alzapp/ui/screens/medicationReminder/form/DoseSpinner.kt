package com.example.alzapp.ui.screens.medicationReminder.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.alzapp.utils.getPolishUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoseSpinner(
    isReadOnly: Boolean,
    selectedNumber: String,
    onNumberChange: (String) -> Unit,
    selectedUnit: String,
    onUnitChange: (String) -> Unit,
) {
    val unitOptions = listOf("tabletek", "ml", "kropli")
    var unitExpanded by remember { mutableStateOf(false) }

    val displayedUnit = remember(selectedNumber, selectedUnit) {
        getPolishUnit(selectedNumber, selectedUnit)
    }

    Column {
        Text(
            text = "Dawka",
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedTextField(
                value = selectedNumber,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() || it == '.' || it == ',' }) onNumberChange(newValue)
                },
                modifier = Modifier.weight(0.7f),
                readOnly = isReadOnly,
                singleLine = true,
                placeholder = { Text("0.5") },
                shape = RoundedCornerShape(50.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = MaterialTheme.typography.bodyLarge,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (isReadOnly) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )

            ExposedDropdownMenuBox(
                expanded = unitExpanded && !isReadOnly,
                onExpandedChange = { if (!isReadOnly) unitExpanded = !unitExpanded },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = displayedUnit,
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    modifier = Modifier
                        .menuAnchor(
                            ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                            enabled = !isReadOnly
                        )
                        .fillMaxWidth(),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(50.dp),
                    textStyle = MaterialTheme.typography.bodyLarge,
                    trailingIcon = {
                        if (!isReadOnly) ExposedDropdownMenuDefaults.TrailingIcon(expanded = unitExpanded)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (isReadOnly) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )

                if (!isReadOnly) {
                    ExposedDropdownMenu(
                        expanded = unitExpanded,
                        onDismissRequest = { unitExpanded = false }
                    ) {
                        unitOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(getPolishUnit(selectedNumber, option)) },
                                onClick = {
                                    onUnitChange(option)
                                    unitExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}