package com.example.alzapp.ui.screens.games.cryptogram

import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.alzapp.R
import com.example.alzapp.ui.screens.games.cryptogram.components.AlphabetKeyboard
import com.example.alzapp.ui.screens.games.cryptogram.components.CryptogramLetterBox
import com.example.alzapp.ui.screens.games.cryptogram.viewModel.CryptogramViewModel
import com.example.alzapp.ui.screens.games.components.ActionButtonGame
import com.example.alzapp.ui.screens.games.components.GameRules
import com.example.alzapp.ui.screens.games.components.Overlay
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CryptogramScreen(
    viewModel: CryptogramViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val view = LocalView.current

    var isPauseMenuVisible by remember { mutableStateOf(false) }
    var showRulesDialog by remember { mutableStateOf(false) }

    val proverb by viewModel.proverb.collectAsState()
    val category by viewModel.category.collectAsState()
    val cipherMap by viewModel.cipherMap.collectAsState()
    val userGuesses by viewModel.userGuesses.collectAsState()
    val keyboardLetters by viewModel.keyboardLetters.collectAsState()
    val completedLetters by viewModel.completedLetters.collectAsState()
    val isSolved by viewModel.isSolved.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val tutorialStep by viewModel.tutorialStep.collectAsState()
    val highlightCipher by viewModel.highlightCipher.collectAsState()

    var selectedCipherNumber by remember { mutableStateOf<Int?>(null) }

    val errorCipherNumber = remember { mutableStateOf<Int?>(null) }
    val successCipherNumber = remember { mutableStateOf<Int?>(null) }

    // pulsing animation for tutorial highlighted info
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "TutorialHighlightAlpha"
    )

    LaunchedEffect(viewModel) {
        viewModel.errorEvent.collectLatest { cipherNo ->
            errorCipherNumber.value = cipherNo
            
            // haptic feedback depending on sdk version
            val constant = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                HapticFeedbackConstants.REJECT
            } else {
                HapticFeedbackConstants.LONG_PRESS
            }
            // decided to keep FLAG_IGNORE_GLOBAL_SETTING even if it's deprecated bc otherwise haptics don't work on the physical device I'm testing
            view.performHapticFeedback(constant, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)

            kotlinx.coroutines.delay(400) // error highlight duration
            if (errorCipherNumber.value == cipherNo) {
                errorCipherNumber.value = null
            }
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.successEvent.collectLatest { cipherNo ->
            successCipherNumber.value = cipherNo
            kotlinx.coroutines.delay(400) // success highlight duration
            if (successCipherNumber.value == cipherNo) {
                successCipherNumber.value = null
            }
        }
    }

    if (showRulesDialog) {
        GameRules(
            gameName = "Ukryte przysłowia",
            rules = listOf(
                "• Twoim zadaniem jest odgadnięcie znanego przysłowia, które zostało ukryte pod szyfrem.",
                "• Każda liczba pod pustym polem odpowiada jednej literze alfabetu.",
                "• Jeśli odgadniesz jedną literę, wskoczy ona we wszystkie miejsca z tym samym numerem."
            ),
            tips = listOf(
                "Spójrz na numer pod okienkiem i wybierz literę z klawiatury, która Twoim zdaniem tam pasuje.",
                "Jeśli wybierzesz poprawnie, litera zostanie na miejscu. Jeśli nie – pole podświetli się na czerwono, dając Ci znać, by spróbować innej.",
                "Nie musisz się spieszyć. Jeśli utkniesz, naciśnij przycisk 'Podpowiedź' – pokaże Ci on właściwą literę.",
                "Szukaj krótkich wyrazów i charakterystycznych końcówek, to pomoże Ci szybciej złamać szyfr!"
            ),
            onDismissRequest = { showRulesDialog = false }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceBright)
                .padding(8.dp)
        ) {
            // top bar
            if (tutorialStep == CryptogramTutorialStep.NONE) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
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
                }
            } else {
                Spacer(modifier = Modifier.height(40.dp))
            }

            if (tutorialStep != CryptogramTutorialStep.NONE) {
                CryptogramTutorialBubble(tutorialStep)
            }

            if (tutorialStep == CryptogramTutorialStep.NONE) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Skojarzenie: $category",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        maxLines = 2
                    )
                }
            }

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (proverb.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Brak haseł. Spróbuj ponownie później.")
                }
            } else {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .then(
                                if (tutorialStep == CryptogramTutorialStep.EXPLAIN_TILES) {
                                    Modifier.border(
                                        width = (4 * pulseAlpha).dp + 2.dp,
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = pulseAlpha),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                } else Modifier
                            ),
                        horizontalArrangement = Arrangement.Center,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        proverb.split(" ").forEach { word ->
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                word.forEach { char ->
                                    val cipherNumber = cipherMap[char]
                                    if (cipherNumber != null) {
                                        val isHighlighted = highlightCipher == cipherNumber
                                        CryptogramLetterBox(
                                            cipherNumber = cipherNumber,
                                            userGuess = userGuesses[cipherNumber],
                                            isSelected = selectedCipherNumber == cipherNumber || isHighlighted,
                                            isError = errorCipherNumber.value == cipherNumber,
                                            isSuccess = successCipherNumber.value == cipherNumber,
                                            onClick = { 
                                                val isAllowed = when (tutorialStep) {
                                                    CryptogramTutorialStep.GUESS_LETTER -> cipherNumber == highlightCipher
                                                    CryptogramTutorialStep.NONE,
                                                    CryptogramTutorialStep.SUCCESS,
                                                    CryptogramTutorialStep.EXPLAIN_HINT -> true
                                                    else -> false
                                                }
                                                if (isAllowed) {
                                                    selectedCipherNumber = cipherNumber 
                                                }
                                            },
                                            modifier = if (isHighlighted) Modifier.border(
                                                width = (4 * pulseAlpha).dp + 2.dp,
                                                color = MaterialTheme.colorScheme.primary.copy(alpha = pulseAlpha),
                                                shape = RoundedCornerShape(4.dp)
                                            ) else Modifier
                                        )
                                    } else {
                                        Text(
                                            text = char.toString(),
                                            style = MaterialTheme.typography.titleLarge,
                                            modifier = Modifier.padding(
                                                vertical = 4.dp,
                                                horizontal = 1.dp
                                            ),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (isSolved) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Gratulacje! Odgadłeś przysłowie!",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp),
                            lineHeight = 38.sp
                        )
                        if (tutorialStep == CryptogramTutorialStep.NONE) {
                            Button(
                                onClick = {
                                    selectedCipherNumber = null
                                    viewModel.loadNewGame()
                                },
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .heightIn(min = 56.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    "Następne przysłowie",
                                    style = MaterialTheme.typography.labelMedium,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 40.sp
                                )
                            }
                        }
                    }
                }

                if (!isSolved || tutorialStep != CryptogramTutorialStep.NONE) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ActionButtonGame(
                            text = "Podpowiedź",
                            icon = ImageVector.vectorResource(R.drawable.icon_hint),
                            onClick = { viewModel.giveHint(targetCipherNumber = selectedCipherNumber) },
                            enabled = tutorialStep == CryptogramTutorialStep.NONE || 
                                      tutorialStep == CryptogramTutorialStep.EXPLAIN_HINT ||
                                      tutorialStep == CryptogramTutorialStep.SUCCESS,
                            modifier = if (tutorialStep == CryptogramTutorialStep.EXPLAIN_HINT) {
                                Modifier.border(
                                    width = (4 * pulseAlpha).dp + 2.dp,
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = pulseAlpha),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            } else Modifier
                        )
                    }

                    AlphabetKeyboard(
                        keyboardLetters = keyboardLetters,
                        completedLetters = completedLetters,
                        onKeyClick = { letter ->
                            if (tutorialStep == CryptogramTutorialStep.NONE || 
                                tutorialStep == CryptogramTutorialStep.GUESS_LETTER ||
                                tutorialStep == CryptogramTutorialStep.SUCCESS) {
                                val target = selectedCipherNumber ?: highlightCipher
                                target?.let { cipherNo ->
                                    viewModel.onGuessChange(cipherNo, letter)
                                }
                            }
                        },
                        highlightedLetter = if (tutorialStep == CryptogramTutorialStep.EXPLAIN_KEYBOARD_DISABLED) 'S' else null,
                        pulseAlpha = pulseAlpha,
                        modifier = if (tutorialStep == CryptogramTutorialStep.EXPLAIN_KEYBOARD) {
                            Modifier.border(
                                width = (4 * pulseAlpha).dp + 2.dp,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = pulseAlpha),
                                shape = RoundedCornerShape(16.dp)
                                )
                        } else Modifier
                    )
                }
            }

            if (tutorialStep != CryptogramTutorialStep.NONE && tutorialStep != CryptogramTutorialStep.GUESS_LETTER) {
                Button(
                    onClick = { viewModel.nextTutorialStep() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = (tutorialStep != CryptogramTutorialStep.SUCCESS || isSolved)
                ) {
                    val btnText = when (tutorialStep) {
                        CryptogramTutorialStep.EXPLAIN_KEYBOARD -> "Zaczynamy szukać!"
                        CryptogramTutorialStep.COMPLETED -> "Rozpocznij grę!"
                        else -> "Idź dalej"
                    }
                    Text(btnText, fontWeight = FontWeight.Bold)
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
