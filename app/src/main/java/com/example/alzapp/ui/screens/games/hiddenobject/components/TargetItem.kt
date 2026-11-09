package com.example.alzapp.ui.screens.games.hiddenobject.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TargetItem(
    name: String,
    isFound: Boolean,
    iconResId: Int? = null,
    pulseAlpha: Float? = null // 0.0 to 1.0
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isFound) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.onPrimary,
        border = when {
            pulseAlpha != null -> BorderStroke((4 * pulseAlpha).dp + 2.dp, MaterialTheme.colorScheme.secondary.copy(alpha = pulseAlpha)) // green pulse
            isFound -> BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
            else -> null
        },
        modifier = Modifier.heightIn(min = 52.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isFound) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Przedmiot znaleziony",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp).padding(end = 4.dp)
                )
            } else if (iconResId != null) {
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp).padding(end = 4.dp),
                    tint = Color.Unspecified
                )
            }
            Text(
                text = name.uppercase(),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = if (isFound) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface,
                lineHeight = 30.sp
            )
        }
    }
}