package com.example.alzapp.ui.screens.user.streaks

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.alzapp.R

@Composable
fun StreakPlantAnimation(streak: Int) {
    // plant growth stage for each streak count (1-7)
    val plantImageRes = when {
        streak <= 0 -> R.drawable.plant_stage_0
        streak == 1 -> R.drawable.plant_stage_1
        streak == 2 -> R.drawable.plant_stage_2
        streak == 3 -> R.drawable.plant_stage_3
        streak == 4 -> R.drawable.plant_stage_4
        streak == 5 -> R.drawable.plant_stage_5
        streak == 6 -> R.drawable.plant_stage_6
        else -> R.drawable.plant_stage_7 // 7+ days; in the future, it could be changed to a new plant after each complete week
    }

    // pulsing plant animation for streak count >= 7
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (streak >= 7) 1.1f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        contentAlignment = Alignment.Center
    ) {
        Crossfade(
            targetState = plantImageRes,
            animationSpec = tween(durationMillis = 800),
            label = "plantTransition"
        ) { targetRes ->
            Image(
                painter = painterResource(id = targetRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .scale(scale),
                contentScale = ContentScale.Fit
            )
        }
    }
}
