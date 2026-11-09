package com.example.alzapp.ui.screens.todo

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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.alzapp.R
import com.example.alzapp.data.todo.local.TodoTaskEntity
import com.example.alzapp.utils.AppConfig

@Composable
fun TodoItem(
    task: TodoTaskEntity,
    isHighlighted: Boolean,
    onCompleteClick: () -> Unit,
    onTaskClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "highlight")

    // card pulsing animation (card scale)
    val cardScale = if (isHighlighted) {
        infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.05f,
            animationSpec = infiniteRepeatable(
                animation = tween(1200, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "cardScale"
        ).value
    } else 1f

    // border highlight pulsing animation
    val borderHighlight = if (isHighlighted) {
        infiniteTransition.animateFloat(
            initialValue = 0.2f,
            targetValue = 0.8f,
            animationSpec = infiniteRepeatable(
                animation = tween(1200, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "border"
        ).value
    } else 0.2f

    // change card color if task is completed
    val containerColor = if (task.isCompleted) MaterialTheme.colorScheme.surfaceVariant
                         else MaterialTheme.colorScheme.onSecondary

    Card(
        onClick = onTaskClick,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 90.dp)
            .graphicsLayer {
                scaleX = cardScale
                scaleY = cardScale
            },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = if (isHighlighted) 3.dp else 1.dp,
            color = if (isHighlighted) MaterialTheme.colorScheme.primary.copy(alpha = borderHighlight) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = if (isHighlighted) 8.dp else 2.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // mark task as completed/uncompleted
            IconButton(
                modifier = Modifier.size(60.dp),
                onClick = onCompleteClick
            ) {
                Icon(
                    imageVector = if (task.isCompleted) ImageVector.vectorResource(R.drawable.icon_checked)
                    else ImageVector.vectorResource(R.drawable.icon_unchecked),
                    contentDescription = if (task.isCompleted) "Oznacz jako niewykonane" else "Oznacz jako wykonane",
                    modifier = Modifier.fillMaxSize(),
                    tint = if (task.isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.width(12.dp))

            Column {
                // displays time if applicable
                if (task.time != null) {
                    Text(
                        text = task.time.format(AppConfig.TIME_FORMATTER),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }
                // displays task title
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
                // displays task description if applicable
                if (task.taskDescription.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = task.taskDescription,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    )
                }
            }
        }
    }
}
