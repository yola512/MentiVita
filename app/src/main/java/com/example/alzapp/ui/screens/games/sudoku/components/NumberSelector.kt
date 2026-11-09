package com.example.alzapp.ui.screens.games.sudoku.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// original source: https://github.com/katarinakovacova/neurodoku
@Composable
fun NumberSelector(
    onNumberSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1st row: 1-5
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (num in 1..5) {
                NumberButton(num, onNumberSelected)
            }
        }
        
        // 2nd row: 6-9
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (num in 6..9) {
                NumberButton(num, onNumberSelected)
            }
        }
    }
}

@Composable
private fun NumberButton(
    num: Int,
    onNumberSelected: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .size(60.dp) // increased size for better touch target
            .clip(RoundedCornerShape(8.dp))
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onNumberSelected(num) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = num.toString(),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}
