package com.example.alzapp.ui.screens.games.hiddenobject

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.alzapp.R
import com.example.alzapp.ui.screens.games.hiddenobject.viewModel.HiddenObjectViewModel
import com.example.alzapp.ui.screens.games.components.Overlay
import com.example.alzapp.ui.screens.games.components.GameRules
import com.example.alzapp.ui.screens.games.hiddenobject.components.AnimatedCircle
import com.example.alzapp.ui.screens.games.hiddenobject.components.TargetItem
import com.example.alzapp.ui.screens.games.hiddenobject.components.TutorialInfoBubble

@Composable
fun HiddenObjectScreen(
    viewModel: HiddenObjectViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val level by viewModel.level.collectAsState()
    val foundObjectIds by viewModel.foundObjectIds.collectAsState()
    val isLevelComplete by viewModel.isLevelComplete.collectAsState()
    val tutorialStep by viewModel.tutorialStep.collectAsState()

    var isPauseMenuVisible by remember { mutableStateOf(false) }
    var showRulesDialog by remember { mutableStateOf(false) }
    var imageSize by remember { mutableStateOf(IntSize.Zero) }
    val gridState = rememberLazyGridState()

    // pulsing animation for tutorial highlights
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f, // min
        targetValue = 0.9f, // max
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "TutorialHighlightAlpha"
    )

    // sorted objects, not found are displayed first (already found are moved to the end of the list)
    val sortedObjects = remember(level.objectsToFind, foundObjectIds) {
        level.objectsToFind.sortedBy { obj ->
            if (obj.id in foundObjectIds) 1 else 0
        }
    }

    // scroll to target item (shovel) in tutorial
    LaunchedEffect(tutorialStep) {
        if (tutorialStep == HiddenObjectTutorialStep.SUCCESS) {
            val shovelIndex = sortedObjects.indexOfFirst { it.id == 2 } // shovel (only object that needs to be found in tutorial)
            if (shovelIndex != -1) {
                gridState.animateScrollToItem(shovelIndex)
            }
        }
    }

    if (showRulesDialog) {
        GameRules(
            gameName = "Oko szpiega",
            rules = listOf(
                "• Przyjrzyj się uważnie ilustracji.",
                "• Znajdź przedmioty wymienione na dolnym panelu.",
                "• Kliknij na obrazek w znaleziony przedmiot, aby go zaznaczyć."
            ),
            tips = listOf(
                "Nie spiesz się, uważnie przeszukuj każdy fragment obrazka.",
                "Jeśli przedmiot jest już znaleziony, zostanie oznaczony na liście poniżej."
            ),
            onDismissRequest = { showRulesDialog = false }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceBright)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            if (tutorialStep == HiddenObjectTutorialStep.NONE) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                        IconButton(
                            onClick = { isPauseMenuVisible = true },
                            modifier = Modifier.size(56.dp)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.icon_pause),
                                contentDescription = "Pauza",
                                modifier = Modifier.size(60.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    Text(
                        text = "PAUZA",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(8.dp))
            }

            // tutorial message
            if (tutorialStep != HiddenObjectTutorialStep.NONE) {
                TutorialInfoBubble(tutorialStep)
            }

            // game area
            Box(
                modifier = Modifier
                    .weight(1.2f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .background(Color(0xFF5D4037))
                        .border(10.dp, Color(0xFF5D4037), RoundedCornerShape(4.dp))
                        .padding(4.dp)
                        .border(2.dp, Color(0xFF3E2723), RoundedCornerShape(2.dp))
                        .onGloballyPositioned { imageSize = it.size }
                        .then(
                            if (tutorialStep == HiddenObjectTutorialStep.FIND_FIRST) {
                                Modifier.border(
                                    width = (4 * pulseAlpha).dp + 2.dp,
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = pulseAlpha),
                                    shape = RoundedCornerShape(4.dp)
                                )
                            } else Modifier
                        )
                ) {
                    if (imageSize.width > 0) {
                        val widthPx = imageSize.width.toFloat()
                        val heightPx = imageSize.height.toFloat()

                        // illustration displayed in game; handling tap gesture
                        AsyncImage(
                            model = level.imageResId,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .pointerInput(Unit) {
                                    detectTapGestures { offset ->
                                        if (!isPauseMenuVisible) {
                                            viewModel.onImageClick(
                                                offset.x,
                                                offset.y,
                                                widthPx,
                                                heightPx
                                            )
                                        }
                                    }
                                },
                            contentScale = ContentScale.FillBounds
                        )

                        // 'draw circle around found object' animation
                        level.objectsToFind.forEach { obj ->
                            if (obj.id in foundObjectIds) {
                                key(obj.id) {
                                    AnimatedCircle(
                                        obj = obj,
                                        width = widthPx,
                                        height = heightPx
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // scrollable list of objects that needs to be found
            Surface(
                tonalElevation = 4.dp,
                modifier = Modifier
                    .weight(0.8f)
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .then(
                        if (tutorialStep == HiddenObjectTutorialStep.EXPLAIN_LIST) {
                            Modifier.border(
                                width = (4 * pulseAlpha).dp + 2.dp,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = pulseAlpha),
                                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                            )
                        } else Modifier
                    ),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.2f)
            ) {
                Column(
                    modifier = Modifier.padding(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "ZNAJDŹ:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(0.8f),
                        thickness = 3.dp,
                        color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.4f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        state = gridState,
                        contentPadding = PaddingValues(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(sortedObjects, key = { it.id }) { obj ->
                            val isFound = obj.id in foundObjectIds
                            val isTutorialTarget = tutorialStep == HiddenObjectTutorialStep.SUCCESS && obj.id == 2
                            TargetItem(
                                name = obj.name,
                                isFound = isFound,
                                iconResId = obj.iconResId,
                                pulseAlpha = if (isTutorialTarget) pulseAlpha else null
                            )
                        }
                    }
                }
            }

            // tutorial action buttons
            if (tutorialStep != HiddenObjectTutorialStep.NONE && tutorialStep != HiddenObjectTutorialStep.FIND_FIRST) {
                Button(
                    onClick = { viewModel.nextTutorialStep() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp)
                        .then(
                            Modifier.border(
                                width = (4 * pulseAlpha).dp + 2.dp,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = pulseAlpha),
                                shape = RoundedCornerShape(12.dp)
                            )
                        ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    val btnText = when (tutorialStep) {
                        HiddenObjectTutorialStep.WELCOME -> "Zaczynajmy!"
                        HiddenObjectTutorialStep.EXPLAIN_LIST -> "Rozumiem"
                        HiddenObjectTutorialStep.EXPLAIN_SEARCH -> "Chcę spróbować!"
                        HiddenObjectTutorialStep.SUCCESS -> "Dalej"
                        HiddenObjectTutorialStep.COMPLETED -> "Graj!"
                        else -> "Dalej"
                    }
                    Text(btnText, fontWeight = FontWeight.Bold)
                }
            }
        }

        // victory message overlay
        if (isLevelComplete) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f))
                    .pointerInput(Unit) {},
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.padding(32.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "BRAWO!",
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.W900,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Udało Ci się znaleźć wszystkie przedmioty!",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge,
                            lineHeight = 36.sp
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        Button(
                            onClick = {
                                viewModel.resetGame()
                                onBackClick()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("ZAKOŃCZ", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        if (isPauseMenuVisible) {
            Overlay(
                onDismiss = { isPauseMenuVisible = false },
                onRules = {
                    isPauseMenuVisible = false
                    showRulesDialog = true
                },
                onTutorial = {
                    isPauseMenuVisible = false
                    viewModel.startTutorial()
                },
                onExit = onBackClick
            )
        }
    }
}
