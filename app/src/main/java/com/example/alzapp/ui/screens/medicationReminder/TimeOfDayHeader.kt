package com.example.alzapp.ui.screens.medicationReminder

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alzapp.R
import com.example.alzapp.ui.screens.medicationReminder.viewModel.TimeOfDay

@Composable
fun TimeOfDayHeader(timeOfDay: TimeOfDay) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        val iconRes = when (timeOfDay) {
            TimeOfDay.MORNING -> R.drawable.icon_morning
            TimeOfDay.AFTERNOON -> R.drawable.icon_midday
            TimeOfDay.EVENING -> R.drawable.icon_evening
            TimeOfDay.NIGHT -> R.drawable.icon_night
        }

        Icon(
            imageVector = ImageVector.vectorResource(iconRes),
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = timeOfDay.title.uppercase(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier.height(2.dp).weight(1f)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
        )
    }
}