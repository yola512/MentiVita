package com.example.alzapp.ui.screens.user.streaks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.alzapp.ui.theme.AlzAppTheme
import kotlinx.coroutines.delay
import java.time.LocalDate

@Composable
fun StreakGrowthDialog(
    currentStreak: Int,
    activityDates: Set<LocalDate>,
    shouldAnimate: Boolean = false,
    onAnimationComplete: () -> Unit = {},
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        StreakGrowthContent(
            currentStreak = currentStreak,
            activityDates = activityDates,
            shouldAnimate = shouldAnimate,
            onAnimationComplete = onAnimationComplete,
            onDismiss = onDismiss
        )
    }
}

@Composable
fun StreakGrowthContent(
    currentStreak: Int,
    activityDates: Set<LocalDate>,
    shouldAnimate: Boolean = false,
    onAnimationComplete: () -> Unit = {},
    onDismiss: () -> Unit
) {
    // start from yesterday's streak level to show today's growth
    var displayedStreak by remember { mutableIntStateOf(if (shouldAnimate) maxOf(0, currentStreak - 1) else currentStreak) }

    LaunchedEffect(shouldAnimate) {
        if (shouldAnimate) {
            delay(500) // initial pause
            displayedStreak = currentStreak
            delay(1000) // wait for transition
            onAnimationComplete()
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Mój Wspaniały Ogród",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            StreakPlantAnimation(streak = displayedStreak)

            Spacer(modifier = Modifier.height(4.dp))

            val streakText = if (currentStreak == 1) "1 dzień z rzędu!" else "$currentStreak dni z rzędu!"
            Text(
                text = streakText,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Spacer(modifier = Modifier.height(16.dp))

            WeeklyTracker(activityDates = activityDates)

            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text(
                    text = "Każda gra to trening dla głowy. Ty dbasz o umysł, a Twój ogród cieszy się i rośnie. Tak trzymaj!",
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    lineHeight = 25.sp,
                    fontWeight = FontWeight.W700
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
                    .heightIn(min = 60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    "Kontynuuj dobrą zabawę",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 30.sp,
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
fun WeeklyTracker(activityDates: Set<LocalDate>) {
    val daysOfWeek = listOf("P", "W", "Ś", "C", "P", "S", "N")
    val today = LocalDate.now()
    val firstDayOfWeek = today.minusDays(today.dayOfWeek.value.toLong() - 1)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 400.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        daysOfWeek.forEachIndexed { index, day ->
            val date = firstDayOfWeek.plusDays(index.toLong())
            val played = activityDates.contains(date)

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            if (played) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (played) {
                        Icon(
                            Icons.Default.WaterDrop,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    } else {
                        Text(day, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                    }
                }
                if (date == today) {
                    Box(modifier = Modifier.size(6.dp).background(MaterialTheme.colorScheme.onSecondaryContainer, CircleShape))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StreakGrowthContentPreview() {
    AlzAppTheme {
        val testStreakCount = 2 // 1-7 to check related pictures
        val monday = LocalDate.now().minusDays(LocalDate.now().dayOfWeek.value.toLong() - 1)
        val dates = listOf(
            monday,
            monday.plusDays(1),
            monday.plusDays(2),
            monday.plusDays(3),
            monday.plusDays(4),
            monday.plusDays(5),
            monday.plusDays(6)
        )
        StreakGrowthContent(
            currentStreak = testStreakCount,
            activityDates = dates.take(testStreakCount).toSet(),
            onDismiss = {}
        )
    }
}
