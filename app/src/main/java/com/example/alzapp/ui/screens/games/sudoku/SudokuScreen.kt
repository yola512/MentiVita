package com.example.alzapp.ui.screens.games.sudoku

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.alzapp.data.games.sudoku.SudokuDifficulty
import com.example.alzapp.data.games.GameDifficulty
import com.example.alzapp.ui.screens.games.sudoku.components.*
import com.example.alzapp.ui.screens.games.sudoku.viewModel.GameEndStatus
import com.example.alzapp.ui.screens.games.sudoku.viewModel.SudokuViewModel
import com.example.alzapp.ui.screens.games.components.GameRules
import com.example.alzapp.ui.screens.games.components.LevelSelectionDialog
import com.example.alzapp.ui.screens.games.components.Overlay

// original source: https://github.com/katarinakovacova/neurodoku
@Composable
fun SudokuScreen(
    viewModel: SudokuViewModel,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    val sudokuGame by viewModel.sudokuGame.collectAsState()
    val selectedCell by viewModel.selectedCell.collectAsState()
    val time by viewModel.time.collectAsState()
    val gameEndStatus by viewModel.gameEndStatus.collectAsState()
    val showSolutions by viewModel.showSolutions.collectAsState()
    val isResultVisible by viewModel.isResultVisible.collectAsState()

    var isPauseMenuVisible by remember { mutableStateOf(false) }
    var showLevelDialog by remember { mutableStateOf(false) }
    var showEndPopup by remember { mutableStateOf(false) }
    var showRulesDialog by remember { mutableStateOf(false) }

    // trigger end-game pop-up
    LaunchedEffect(gameEndStatus) {
        if (gameEndStatus != GameEndStatus.NONE && !isResultVisible) {
            showEndPopup = true
        }
    }

    // format time + timer logic
    val minutes = (time / 60).toString().padStart(2, '0')
    val seconds = (time % 60).toString().padStart(2, '0')

    val startTimer = {
        viewModel.startTimer()
        isPauseMenuVisible = false
    }
    val stopTimer = {
        if (gameEndStatus == GameEndStatus.NONE) {
            viewModel.stopTimer()
            isPauseMenuVisible = true
        }
    }

    // automatically stop/resume timer
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE, Lifecycle.Event.ON_STOP -> stopTimer()
                Lifecycle.Event.ON_RESUME -> if (!isPauseMenuVisible && gameEndStatus == GameEndStatus.NONE) startTimer()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    if (showLevelDialog) {
        LevelSelectionDialog(
            onDismissRequest = { showLevelDialog = false },
            onLevelSelected = { selectedDifficulty ->
                showLevelDialog = false
                val sudokuDifficulty = when(selectedDifficulty) {
                    GameDifficulty.EASY -> SudokuDifficulty.EASY
                    GameDifficulty.MEDIUM -> SudokuDifficulty.MEDIUM
                    GameDifficulty.HARD -> SudokuDifficulty.HARD
                    GameDifficulty.EXTREME -> SudokuDifficulty.EXTREME
                }
                viewModel.generateNewGame(sudokuDifficulty)
            }
        )
    }

    if (showRulesDialog) {
        GameRules(
            gameName = "Sudoku",
            rules = listOf("• Każdy wiersz musi zawierać cyfry od 1 do 9 bez powtórzeń.",
                    "• Każda kolumna musi zawierać cyfry od 1 do 9 bez powtórzeń.",
                    "• Każdy z dziewięciu kwadratów 3x3 musi zawierać cyfry od 1 do 9 bez powtórzeń."),
            tips = listOf(
                "Wybierz pole na planszy, aby je zaznaczyć.",
                "Wybierz cyfrę z dolnego panelu, aby wpisać ją w pole.",
                "Użyj przycisku 'Usuń', aby wyczyścić błędnie wpisaną cyfrę.",
                "Jeśli utkniesz, użyj przycisku 'Podpowiedź'."
            ),
            onDismissRequest = { showRulesDialog = false }
        )
    }

    // end-game pop-up (when user finishes the whole board)
    if (showEndPopup) {
        AlertDialog(
            onDismissRequest = { 
                showEndPopup = false
                viewModel.onEndGamePopupDismissed()
            },
            title = {
                Text(
                    text = when (gameEndStatus) {
                        GameEndStatus.PERFECT -> "Gratulacje!"
                        GameEndStatus.PARTIAL -> "Dobra robota!"
                        else -> "Koniec gry"
                    },
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            text = {
                val message = when (gameEndStatus) {
                    GameEndStatus.PERFECT -> "Super! Udało Ci się uzupełnić sudoku w czasie: $minutes:$seconds"
                    GameEndStatus.PARTIAL -> "Super! Udało Ci się częściowo poprawnie uzupełnić sudoku. Wróć ponownie i spróbuj jeszcze raz zmierzyć się z kolejnym sudoku!"
                    else -> "Super, próbowałeś! Spróbuj następnym razem, do zobaczenia!"
                }
                Text(text = message, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyMedium, lineHeight = 1.5.em)
            },
            confirmButton = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Button(onClick = { 
                        showEndPopup = false
                        viewModel.onEndGamePopupDismissed()
                    }) { Text("OK", style = MaterialTheme.typography.labelLarge) }
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    Box(modifier = modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.surfaceBright
    )) {
        // game
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            UpperActionBar(
                onNewGame = { showLevelDialog = true },
                onPause = { 
                    viewModel.stopTimer()
                    isPauseMenuVisible = true
                },
                difficulty = sudokuGame?.difficulty ?: SudokuDifficulty.MEDIUM
            )

            Spacer(modifier = Modifier.height(16.dp))

            sudokuGame?.let { game ->
                SudokuGrid(
                    sudoku = game.userGrid,
                    originalCells = game.maskedGrid,
                    selectedCell = selectedCell,
                    isResultVisible = isResultVisible,
                    showSolutions = showSolutions,
                    completeGrid = game.completeGrid,
                    onCellClick = { row, col ->
                        if (!game.maskedGrid[row][col] && !isResultVisible) {
                            viewModel.selectCell(row, col)
                        }
                    }
                )
            } ?: run {
                Text(text = "Ładowanie Sudoku...", style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isResultVisible) {
                Button(
                    onClick = { viewModel.toggleShowSolutions() },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        if (showSolutions) "Zobacz swoje rozwiązanie" else "Zobacz poprawne rozwiązanie",
                        style = MaterialTheme.typography.labelLarge, textAlign = TextAlign.Center,
                        lineHeight = 30.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = onBackClick) {
                    Text("Wyjście z gry", style = MaterialTheme.typography.labelLarge)
                }
            } else {
                LowerActionBar(
                    onErase = { selectedCell?.let { (row, col) -> viewModel.eraseNumber(row, col) } },
                    onHint = { selectedCell?.let { (row, col) -> viewModel.getHint(row, col) } },
                )
                Spacer(modifier = Modifier.height(16.dp))
                NumberSelector(onNumberSelected = { number ->
                    selectedCell?.let { (row, col) -> viewModel.setNumber(row, col, number) }
                })
                Spacer(modifier = Modifier.height(16.dp))
                Text("© Copyright (c) 2025 Katarina Kovacova", textAlign = TextAlign.Center, fontSize = 12.sp)
            }
        }

        // overlay
        if (isPauseMenuVisible) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Overlay(
                    onDismiss = {
                        if (gameEndStatus == GameEndStatus.NONE) startTimer()
                        else isPauseMenuVisible = false
                    },
                    onRules = {
                        isPauseMenuVisible = false
                        showRulesDialog = true
                    },
                    onTutorial = {},
                    onExit = onBackClick
                )
            }
        }
    }
}
