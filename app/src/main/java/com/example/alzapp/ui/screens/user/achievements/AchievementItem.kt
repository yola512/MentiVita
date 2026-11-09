package com.example.alzapp.ui.screens.user.achievements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alzapp.data.user.local.AchievementEntity

@Composable
fun AchievementItem(achievement: AchievementEntity) {
    val grayMatrix = remember { ColorMatrix().apply { setToSaturation(0f) }}

    val isUnlocked = achievement.isUnlocked
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(
                    color = if (isUnlocked) MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
                            else MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = achievement.iconRes),
                contentDescription = achievement.title,
                modifier = Modifier.fillMaxSize(),
                colorFilter =
                    if (isUnlocked) null
                    else ColorFilter.colorMatrix(grayMatrix)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = achievement.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = if (isUnlocked) MaterialTheme.colorScheme.onSurface else Color.Gray
        )

        Text(
            text = achievement.description,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp,
            color = if (isUnlocked) Color.DarkGray else Color.Gray.copy(alpha = 0.7f),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}