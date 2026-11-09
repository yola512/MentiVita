package com.example.alzapp.ui.screens.games.sudoku.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// original source: https://github.com/katarinakovacova/neurodoku
@Composable
fun SudokuCell(
    modifier: Modifier = Modifier,
    number: Int?,
    isSelected: Boolean,
    isOriginal: Boolean,
    hasRightBorder: Boolean,
    hasBottomBorder: Boolean,
    onClick: () -> Unit,
    statusBackgroundColor: Color? = null,
    isBlocked: Boolean = false
) {
    val backgroundColor = when {
        statusBackgroundColor != null -> statusBackgroundColor
        isSelected -> MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
        isOriginal -> MaterialTheme.colorScheme.surfaceVariant
        else -> MaterialTheme.colorScheme.surface
    }

    val textColor = when {
        isOriginal -> MaterialTheme.colorScheme.onSurface
        statusBackgroundColor != null && statusBackgroundColor.copy(alpha = 1f) == Color.Red -> Color.Red
        statusBackgroundColor != null && statusBackgroundColor.copy(alpha = 1f) == MaterialTheme.colorScheme.secondary-> MaterialTheme.colorScheme.onSecondaryContainer
        else -> MaterialTheme.colorScheme.onSecondaryContainer
    }

    val borderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(backgroundColor)
            .clickable(enabled = !isOriginal && !isBlocked, onClick = onClick)
            .drawBehind {
                val normalBorder = 1.dp.toPx()
                val thickBorder = 2.5.dp.toPx()

                // draw normal borders
                drawLine(borderColor, Offset(0f, 0f), Offset(size.width, 0f), normalBorder)
                drawLine(borderColor, Offset(0f, 0f), Offset(0f, size.height), normalBorder)
                drawLine(borderColor, Offset(size.width, 0f), Offset(size.width, size.height), normalBorder)
                drawLine(borderColor, Offset(0f, size.height), Offset(size.width, size.height), normalBorder)

                // draw thick borders if applicable
                if (hasRightBorder) {
                    drawLine(borderColor, Offset(size.width, 0f), Offset(size.width, size.height), thickBorder)
                }
                if (hasBottomBorder) {
                    drawLine(borderColor, Offset(0f, size.height), Offset(size.width, size.height), thickBorder)
                }
            }
    ) {
        if (number != null) {
            Text(
                text = number.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = textColor
            )
        }
    }
}