package com.example.alzapp.ui.screens.games.postman

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.alzapp.R
import com.example.alzapp.data.games.GameDifficulty
import com.example.alzapp.ui.screens.games.postman.components.FlyingLetter
import com.example.alzapp.ui.screens.games.postman.components.ProgressRoad
import com.example.alzapp.ui.screens.games.postman.viewModel.PostmanViewModel
import com.example.alzapp.ui.screens.games.components.ActionButtonGame
import com.example.alzapp.ui.screens.games.components.GameRules
import com.example.alzapp.ui.screens.games.components.LevelSelectionDialog
import com.example.alzapp.ui.screens.games.components.Overlay

@Composable
fun PostmanScreen(
    viewModel: PostmanViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val n by viewModel.n.collectAsState()
    val currentHouseColor by viewModel.currentHouseColor.collectAsState()
    val history by viewModel.history.collectAsState()
    val lettersDelivered by viewModel.lettersDelivered.collectAsState()
    val totalDeliverable by viewModel.totalDeliverable.collectAsState()
    val feedbackMessage by viewModel.feedbackMessage.collectAsState()
    val showLetterAnimation by viewModel.showLetterAnimation.collectAsState()
    val isGameFinished by viewModel.isGameFinished.collectAsState()
    val progress by viewModel.progress.collectAsState()
    val tutorialStep by viewModel.tutorialStep.collectAsState()

    var isPauseMenuVisible by remember { mutableStateOf(false) }
    var showLevelDialog by remember { mutableStateOf(false) }
    var showRulesDialog by remember { mutableStateOf(false) }
    
    // pulsing animation for tutorial highlights
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    if (showLevelDialog) {
        LevelSelectionDialog(
            onDismissRequest = { showLevelDialog = false },
            labelProvider = { level ->
                when(level) {
                    GameDifficulty.EASY -> "1 w tył"
                    GameDifficulty.MEDIUM -> "2 w tył"
                    GameDifficulty.HARD -> "3 w tył"
                    GameDifficulty.EXTREME -> "4 w tył"
                }
            },
            onLevelSelected = { difficulty ->
                val selectedN = when(difficulty) {
                    GameDifficulty.EASY -> 1
                    GameDifficulty.MEDIUM -> 2
                    GameDifficulty.HARD -> 3
                    GameDifficulty.EXTREME -> 4
                }
                viewModel.startGame(selectedN)
                showLevelDialog = false
            }
        )
    }

    if (showRulesDialog) {
        GameRules(
            gameName = "Listonosza",
            rules = listOf("• Twoim zadaniem jest dostarczanie listów do odpowiednich domów.",
                    "• Zapamiętaj kolor domu, który widzisz.",
                    "• Kiedy pojawi się kolejny dom, sprawdź czy ma taki sam kolor jak dom sprzed N kroków."),
            tips = listOf("Naciśnij 'To ten sam kolor!', jeśli kolory się zgadzają. Pomarańczowy przycisk pojawi się kiedy będzie możliwość porównania koloru domu sprzed n kroków z kolorem obecnego.",
                   "Naciśnij 'Jedź dalej', aby zobaczyć kolejny dom.",
                   "Możesz zmienić poziom trudności w prawym górnym rogu",
                   "Poziom trudności związany jest z ilością domów, które musisz zapamiętać. Przykładowo poziom 2 w tył oznacza, że gracz musi zapamiętać kolory dwóch domków w tył."),
            onDismissRequest = { showRulesDialog = false }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceBright)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // upper action bar - hidden during tutorial except for WELCOME step
            if (tutorialStep == PostmanTutorialStep.NONE) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        IconButton(
                            onClick = { isPauseMenuVisible = true },
                            modifier = Modifier.size(60.dp)
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

                    ActionButtonGame(
                        text = "Poziom",
                        onClick = { showLevelDialog = true },
                        icon = ImageVector.vectorResource(R.drawable.icon_add)
                    )
                }

                Text(
                    text = "Dostarczone listy: $lettersDelivered",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 14.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
                ProgressRoad(progress = progress)
            } else {
                // space for tutorial message
                Spacer(modifier = Modifier.height(40.dp))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = currentHouseColor,
                    transitionSpec = {
                        (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                            slideOutHorizontally { width -> -width } + fadeOut()
                        )
                    },
                    label = "HouseAnimation"
                ) { color ->
                    if (color != null) {
                        HouseIcon(
                            color = color.color,
                            modifier = Modifier.fillMaxSize(0.85f)
                        )
                    }
                }

                if (showLetterAnimation) {
                    FlyingLetter()
                }
            }

            // tutorial info bubble
            Box(
                modifier = Modifier
                    .heightIn(min = 120.dp)
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                val message = when (tutorialStep) {
                    PostmanTutorialStep.WELCOME -> "Cześć! Pomożesz mi dostarczyć listy do tych kolorowych domów?"
                    PostmanTutorialStep.SHOW_FIRST -> "To nasz pierwszy dom. Zapamiętaj, że jest ${currentHouseColor?.displayName}."
                    PostmanTutorialStep.SHOW_MATCH -> "O! Ten dom ma taki sam kolor jak ten przed chwilą! Kliknij pomarańczowy przycisk."
                    PostmanTutorialStep.SUCCESS_HIT -> "Brawo! List został dostarczony. Widzisz latającą kopertę? Teraz możemy jechać do kolejnego domu!"
                    PostmanTutorialStep.COMPLETED -> "Wspaniale! Znasz już całą trasę. Teraz spróbuj samodzielnie rozwieźć resztę listów."
                    else -> feedbackMessage
                }

                message?.let { msg ->
                    Surface(
                        color = MaterialTheme.colorScheme.onPrimary,
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomEnd = 24.dp, bottomStart = 4.dp),
                        shadowElevation = 6.dp,
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = msg,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onTertiary,
                            modifier = Modifier.padding(20.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val isMatchStep = tutorialStep == PostmanTutorialStep.SHOW_MATCH
                val canMatch = history.size > n || tutorialStep != PostmanTutorialStep.NONE
                // "it's a match" button highlighted
                Button(
                    onClick = { viewModel.onMatchClick() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .then(
                            if (isMatchStep) Modifier.border(
                                width = (4 * pulseAlpha).dp + 2.dp,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = pulseAlpha),
                                shape = RoundedCornerShape(16.dp)
                            ) else Modifier
                        ),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.outlineVariant),
                    elevation = ButtonDefaults.buttonElevation(4.dp),
                    shape = RoundedCornerShape(16.dp),
                    enabled = (isMatchStep || tutorialStep == PostmanTutorialStep.NONE) && canMatch
                ) {
                    Text("To ten sam kolor!", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }

                // next step button (highlighted) in all steps apart from NONE and SHOW_MATCH
                val isNextStep = tutorialStep != PostmanTutorialStep.NONE && tutorialStep != PostmanTutorialStep.SHOW_MATCH
                
                OutlinedButton(
                    onClick = { 
                        if (tutorialStep != PostmanTutorialStep.NONE) viewModel.nextTutorialStep()
                        else viewModel.onNextClick() 
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 64.dp)
                        .then(
                            if (isNextStep) Modifier.border(
                                width = (4 * pulseAlpha).dp + 2.dp,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = pulseAlpha),
                                shape = RoundedCornerShape(16.dp)
                            ) else Modifier
                        ),
                    shape = RoundedCornerShape(16.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(width = 2.dp),
                    enabled = tutorialStep != PostmanTutorialStep.SHOW_MATCH
                ) {
                    val nextText = when (tutorialStep) {
                        PostmanTutorialStep.WELCOME -> "Chętnie pomogę!"
                        PostmanTutorialStep.SHOW_FIRST -> "Zapamiętałem!"
                        PostmanTutorialStep.SUCCESS_HIT -> "Jedź do następnego domu"
                        PostmanTutorialStep.COMPLETED -> "Zaczynamy!"
                        else -> "Jedź dalej"
                    }
                    Text(nextText, style = MaterialTheme.typography.bodyLarge)
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        // victory message overlay
        if (isGameFinished) {
            AlertDialog(
                onDismissRequest = { },
                title = {
                    Text(
                        text = "Dobra robota!",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                text = {
                    Text(
                        text = "Koniec trasy, udało ci się dostarczyć $lettersDelivered/$totalDeliverable listów.",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 1.5.em,
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                confirmButton = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Button(
                            onClick = onBackClick,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Koniec pracy na dziś", style = MaterialTheme.typography.labelLarge, textAlign = TextAlign.Center, lineHeight = 1.3.em)
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface
            )
        }

        if (isPauseMenuVisible) {
            Overlay(
                onDismiss = { isPauseMenuVisible = false },
                onTutorial = {
                    isPauseMenuVisible = false
                    viewModel.startTutorial() 
                },
                onRules = {
                    isPauseMenuVisible = false
                    showRulesDialog = true
                },
                onExit = onBackClick
            )
        }
    }
}

@Composable
fun HouseIcon(color: Color, modifier: Modifier) {
    Icon(
        imageVector = ImageVector.vectorResource(R.drawable.icon_house_game),
        contentDescription = "Dom",
        modifier = modifier.sizeIn(minWidth = 200.dp, maxWidth = 400.dp),
        tint = color
    )
}
