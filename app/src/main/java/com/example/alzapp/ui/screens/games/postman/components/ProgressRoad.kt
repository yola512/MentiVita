package com.example.alzapp.ui.screens.games.postman.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.alzapp.R
import com.example.alzapp.ui.theme.AlzAppTheme

@Composable
fun ProgressRoad(progress: Float) {
    // smooth progress road animation
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "ProgressAnimation"
    )

    // driving animation
    val infiniteTransition = rememberInfiniteTransition(label = "TruckVibration")
    val vibrationOffset by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(150, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "VibrationOffset"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
            trackColor = Color.Transparent
        )
        // delivery truck moving on the progress bar
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProgress.coerceIn(0.01f, 1f))
                .fillMaxHeight(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.icon_delivery_truck),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 4.dp)
                    .graphicsLayer {
                        // "vibration" to simulate moving vehicle
                        translationY = vibrationOffset
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProgressRoadPreview() {
    AlzAppTheme {
        ProgressRoad(0.3f)
    }
}
