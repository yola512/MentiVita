package com.example.alzapp.ui.screens.games.hiddenobject.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.alzapp.data.games.hiddenObjects.model.HiddenObject

@Composable
fun AnimatedCircle(
    obj: HiddenObject,
    width: Float,
    height: Float
) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val minDim = minOf(width, height)
        val centerX = (obj.xPercent / 100f) * width
        val centerY = (obj.yPercent / 100f) * height
        val radius = maxOf((obj.radiusPercent / 100f) * minDim * 1.15f, 15.dp.toPx())

        val circlePath = Path().apply {
            addOval(Rect(center = Offset(centerX, centerY), radius = radius))
        }

        val pathMeasure = PathMeasure()
        pathMeasure.setPath(circlePath, false)
        val partialPath = Path()
        pathMeasure.getSegment(0f, progress.value * pathMeasure.length, partialPath, true)

        drawPath(
            path = partialPath,
            color = Color.Red,
            style = Stroke(width = 4.dp.toPx())
        )
    }
}