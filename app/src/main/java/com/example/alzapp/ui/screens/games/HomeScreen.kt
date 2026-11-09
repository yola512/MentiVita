package com.example.alzapp.ui.screens.games

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.alzapp.R
import com.example.alzapp.data.games.GameCategory
import com.example.alzapp.ui.screens.user.achievements.AchievementsDialog
import com.example.alzapp.ui.screens.user.streaks.StreakGrowthDialog
import com.example.alzapp.ui.components.CategoryHeader
import com.example.alzapp.ui.components.CustomCard
import com.example.alzapp.utils.GameIds
import java.time.LocalDate

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onGameClick: (Int) -> Unit,
) {
    val userStats by viewModel.userStats.collectAsState()
    val achievements by viewModel.achievements.collectAsState()
    val hasSeenAnimation by viewModel.hasSeenGrowthAnimation.collectAsState()
    
    var showStreakDialog by remember { mutableStateOf(false) }
    var showAchievementsDialog by remember { mutableStateOf(false) }

    if (showStreakDialog) {
        val playedToday = userStats.activityDates.contains(LocalDate.now())
        
        StreakGrowthDialog(
            currentStreak = userStats.currentStreak,
            activityDates = userStats.activityDates,
            shouldAnimate = playedToday && !hasSeenAnimation,
            onAnimationComplete = { viewModel.markGrowthAnimationAsSeen() },
            onDismiss = { showStreakDialog = false }
        )
    }

    if (showAchievementsDialog) {
        AchievementsDialog(
            achievements = achievements,
            onDismiss = { showAchievementsDialog = false }
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // header section with streaks and achievements
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // streak button
                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.tertiaryContainer, RoundedCornerShape(24.dp))
                        .clickable { showStreakDialog = true }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.icon_streak),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = "${userStats.currentStreak} dni",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }

                // achievements button
                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(24.dp))
                        .clickable { 
                            showAchievementsDialog = true
                        }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.icon_achievement),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = "MEDALE",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            // all available games
            Text(
                text = "GRY",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.displayLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(30.dp),
                contentPadding = PaddingValues(start = 16.dp, end = 0.dp, bottom = 24.dp) // end = 0 for decorative line to reach the edge
            ) {
                items(GameCategory.entries) { category ->
                    Column {
                        CategoryHeader(
                            title = category.displayName,
                            modifier = Modifier.padding(bottom = 13.dp)
                        )
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(40.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp)
                        ) {
                            when (category) {
                                GameCategory.LOGIC -> {
                                    item {
                                        CustomCard(
                                            name = "Sudoku",
                                            imageRes = R.drawable.sudoku_game,
                                            onClick = { onGameClick(GameIds.SUDOKU) }
                                        )
                                    }
                                }
                                GameCategory.MEMORY -> {
                                    item {
                                        CustomCard(
                                            name = "Listonosz",
                                            imageRes = R.drawable.postman_game,
                                            onClick = { onGameClick(GameIds.POSTMAN) }
                                        )
                                    }
                                }
                                GameCategory.ATTENTION -> {
                                    item {
                                        CustomCard(
                                            name = "Oko szpiega",
                                            imageRes = R.drawable.hidden_objects_3,
                                            onClick = { onGameClick(GameIds.HIDDEN_OBJECTS) }
                                        )
                                    }
                                }
                                GameCategory.VOCABULARY -> {
                                    item {
                                        CustomCard(
                                            name = "Ukryte przysłowia",
                                            imageRes = R.drawable.cryptogram_game,
                                            onClick = { onGameClick(GameIds.PROVERB) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
