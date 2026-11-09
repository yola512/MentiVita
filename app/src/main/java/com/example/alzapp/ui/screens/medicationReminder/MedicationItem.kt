package com.example.alzapp.ui.screens.medicationReminder

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alzapp.R
import com.example.alzapp.ui.screens.medicationReminder.viewModel.MedicationStatus
import com.example.alzapp.ui.theme.AlzAppTheme

@Composable
fun MedicationItem(
    medicationName: String,
    dose: String,
    takingTime: String,
    isTaken: Boolean,
    isHighlighted: Boolean = false,
    status: MedicationStatus = MedicationStatus.CanTake,
    onTakeClick: () -> Unit,
    onMedicationClick: () -> Unit
) {
    val infiniteTransition =
        if (isHighlighted) rememberInfiniteTransition(label = "highlight") else null

    val scale = infiniteTransition?.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )?.value
        ?: 1f

    val borderAlpha = infiniteTransition?.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "borderAlpha"
    )?.value
        ?: 0.2f

    val isEnabled = when (status) {
        is MedicationStatus.CanTake -> true
        is MedicationStatus.AlreadyTaken -> status.canUndo
        else -> false
    }

    val containerColor = when {
        isTaken -> MaterialTheme.colorScheme.surfaceVariant
        status is MedicationStatus.TooEarly -> MaterialTheme.colorScheme.surfaceContainerLow
        else -> MaterialTheme.colorScheme.onSecondary
    }

    Card(
        onClick = onMedicationClick,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 90.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = if (isHighlighted) 3.dp else 1.dp,
            color = if (isHighlighted) MaterialTheme.colorScheme.primary.copy(alpha = borderAlpha) else MaterialTheme.colorScheme.onSurface.copy(
                alpha = 0.12f
            )
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = if (isHighlighted) 8.dp else 2.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .alpha(if (status is MedicationStatus.TooEarly) 0.6f else 1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier.size(60.dp),
                onClick = onTakeClick,
                enabled = isEnabled
            ) {
                Icon(
                    imageVector = if (isTaken) ImageVector.vectorResource(R.drawable.icon_checked)
                    else ImageVector.vectorResource(R.drawable.icon_medications),
                    contentDescription = if (isTaken) {
                        if (isEnabled) "Lek przyjęty. Naciśnij, aby cofnąć zaznaczenie" else "Lek przyjęty"
                    } else "Naciśnij, aby potwierdzić przyjęcie leku",
                    modifier = Modifier.fillMaxSize(),
                    tint = if (isTaken) {
                        if (isEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    } else MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = medicationName,
                    style = MaterialTheme.typography.titleLarge,
                    lineHeight = 30.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textDecoration = if (isTaken) TextDecoration.LineThrough else TextDecoration.None
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$dose, o $takingTime",
                    style = MaterialTheme.typography.titleMedium,
                    lineHeight = 30.sp,
                    fontWeight = FontWeight.W600,
                    color = MaterialTheme.colorScheme.onSurface,
                    textDecoration = if (isTaken) TextDecoration.LineThrough else TextDecoration.None
                )

                when (status) {
                    is MedicationStatus.TooEarly -> {
                        Text(
                            text = status.message,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 30.sp
                        )
                    }

                    is MedicationStatus.TooLate -> {
                        Text(
                            text = status.message,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 30.sp
                        )
                    }

                    is MedicationStatus.AlreadyTaken -> {
                        if (!status.canUndo) {
                            Text(
                                text = "Lek przyjęty",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium,
                                lineHeight = 30.sp
                            )
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MedicationItemPreview() {
    AlzAppTheme {
        MedicationItem(
            medicationName = "Ixymil",
            dose = "2 tabletki",
            takingTime = "13:00",
            isTaken = true,
            onTakeClick = {},
            onMedicationClick = {}
        )
    }
}