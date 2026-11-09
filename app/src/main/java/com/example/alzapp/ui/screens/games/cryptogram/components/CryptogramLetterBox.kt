package com.example.alzapp.ui.screens.games.cryptogram.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CryptogramLetterBox(
    cipherNumber: Int,
    userGuess: Char?,
    isSelected: Boolean,
    isError: Boolean,
    isSuccess: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "shake")
    // shake animation for wrong letter choice
    val shakeOffset by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(80, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shakeOffset"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(1.dp)
            .width(36.dp)
            .graphicsLayer {
                if (isError) {
                    translationX = shakeOffset.dp.toPx()
                }
            }
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    when {
                        isSuccess -> Color(0xFFC8E6C9)
                        isError -> Color(0xFFFFCDD2)
                        isSelected -> Color(0xFFFFE082)
                        else -> MaterialTheme.colorScheme.surface
                    },
                    RoundedCornerShape(4.dp)
                )
                .border(
                    width = if (isSelected || isError || isSuccess) 2.dp else 1.dp,
                    color = when {
                        isSuccess -> MaterialTheme.colorScheme.secondary
                        isError -> MaterialTheme.colorScheme.error
                        isSelected -> MaterialTheme.colorScheme.tertiary
                        else -> MaterialTheme.colorScheme.onSurface
                    },
                    shape = RoundedCornerShape(4.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = userGuess?.toString() ?: "",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Text(
            text = cipherNumber.toString(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 2.dp),
            fontWeight = FontWeight.Bold
        )
    }
}