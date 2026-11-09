package com.example.alzapp.ui.screens.games.sudoku.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alzapp.data.user.AchievementRepository
import com.example.alzapp.data.games.sudoku.SudokuDifficulty
import com.example.alzapp.data.games.sudoku.local.SudokuEntity
import com.example.alzapp.data.games.sudoku.SudokuGenerator
import com.example.alzapp.data.games.sudoku.SudokuRepository
import com.example.alzapp.data.user.UserStatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.System.currentTimeMillis
import javax.inject.Inject

// original source: https://github.com/katarinakovacova/neurodoku
enum class GameEndStatus {
    NONE, PERFECT, PARTIAL, FAILED
}

@HiltViewModel
class SudokuViewModel @Inject constructor(
    private val sudokuGenerator: SudokuGenerator,
    private val sudokuRepository: SudokuRepository,
    private val userStatsRepository: UserStatsRepository,
    private val achievementRepository: AchievementRepository
) : ViewModel() {

    private val _sudokuGame = MutableStateFlow<SudokuEntity?>(null)
    val sudokuGame: StateFlow<SudokuEntity?> = _sudokuGame.asStateFlow()

    private val _selectedCell = MutableStateFlow<Pair<Int, Int>?>(null)
    val selectedCell: StateFlow<Pair<Int, Int>?> = _selectedCell.asStateFlow()

    private val _time = MutableStateFlow(0L)
    val time: StateFlow<Long> = _time.asStateFlow()

    private val _gameEndStatus = MutableStateFlow(GameEndStatus.NONE)
    val gameEndStatus: StateFlow<GameEndStatus> = _gameEndStatus.asStateFlow()

    private val _isResultVisible = MutableStateFlow(false)
    val isResultVisible: StateFlow<Boolean> = _isResultVisible.asStateFlow()

    private val _showSolutions = MutableStateFlow(false)
    val showSolutions: StateFlow<Boolean> = _showSolutions.asStateFlow()

    private var timerJob: Job? = null
    private var autosaveJob: Job? = null

    init {
        viewModelScope.launch {
            val loadedGame = sudokuRepository.getUnfinishedSudoku()
            if (loadedGame != null) {
                _sudokuGame.value = loadedGame
                _time.value = loadedGame.timeSpent
                if (!loadedGame.isCompleted) startTimer()
            } else {
                generateNewGame(SudokuDifficulty.MEDIUM)
            }
        }

        observeAndAutosave()
    }

    fun generateNewGame(difficulty: SudokuDifficulty) {
        val newGame = sudokuGenerator.generateNewGame(difficulty)
        _sudokuGame.value = newGame
        _selectedCell.value = null
        _time.value = 0L
        _gameEndStatus.value = GameEndStatus.NONE
        _isResultVisible.value = false
        _showSolutions.value = false
        resetTimer()
        startTimer()
    }

    fun selectCell(row: Int, col: Int) {
        if (_gameEndStatus.value != GameEndStatus.NONE || _isResultVisible.value) return
        _selectedCell.value = Pair(row, col)
    }

    fun setNumber(row: Int, col: Int, number: Int?) {
        val game = _sudokuGame.value ?: return
        if (game.maskedGrid[row][col] || _gameEndStatus.value != GameEndStatus.NONE || _isResultVisible.value) return

        val newUserGrid = game.userGrid.mapIndexed { r, rowArray ->
            if (r == row) {
                rowArray.mapIndexed { c, value ->
                    if (c == col) number else value
                }.toTypedArray()
            } else {
                rowArray
            }
        }.toTypedArray()

        val updated = game.copy(userGrid = newUserGrid)
        _sudokuGame.value = updated
        
        if (isGridFull(updated)) {
            checkGameEnd(updated)
        }
    }

    private fun isGridFull(game: SudokuEntity): Boolean {
        return game.userGrid.all { row -> row.all { it != null } }
    }

    private fun checkGameEnd(game: SudokuEntity) {
        stopTimer()
        var correctCount = 0
        var userCells = 0

        for (r in 0..8) {
            for (c in 0..8) {
                if (game.userGrid[r][c] == game.completeGrid[r][c]) {
                    correctCount++
                }
                if (!game.maskedGrid[r][c]) userCells++
            }
        }

        val status = when {
            correctCount == 81 -> GameEndStatus.PERFECT
            correctCount > (81 - userCells / 2) -> GameEndStatus.PARTIAL
            else -> GameEndStatus.FAILED
        }

        _gameEndStatus.value = status
        
        viewModelScope.launch {
            userStatsRepository.updateStreak()
            if (status == GameEndStatus.PERFECT) {
                achievementRepository.unlockAchievement("sudoku_1")
            }
        }
    }

    fun onEndGamePopupDismissed() {
        _isResultVisible.value = true
        _selectedCell.value = null

        if (_gameEndStatus.value != GameEndStatus.NONE) {
            updateGame {
                it.copy(isCompleted = true, completedAt = currentTimeMillis())
            }
        }
    }

    fun toggleShowSolutions() {
        _showSolutions.value = !_showSolutions.value
    }

    fun getHint(row: Int, col: Int) {
        val game = _sudokuGame.value ?: return
        if (game.maskedGrid[row][col] || _gameEndStatus.value != GameEndStatus.NONE || _isResultVisible.value) return

        val hintNumber = game.completeGrid[row][col]

        val newUserGrid = game.userGrid.mapIndexed { r, rowArray ->
            if (r == row) {
                rowArray.mapIndexed { c, value ->
                    if (c == col) hintNumber else value
                }.toTypedArray()
            } else {
                rowArray
            }
        }.toTypedArray()

        updateGame { it.copy(userGrid = newUserGrid) }
    }

    fun eraseNumber(row: Int, col: Int) {
        setNumber(row, col, null)
    }

    fun startTimer() {
        if (_gameEndStatus.value != GameEndStatus.NONE || _isResultVisible.value) return
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _time.value += 1
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
    }

    private fun resetTimer() {
        _time.value = 0L
        stopTimer()
    }

    private fun updateGame(modifier: (SudokuEntity) -> SudokuEntity) {
        val game = _sudokuGame.value ?: return
        val updated = modifier(game)
        _sudokuGame.value = updated
    }

    @OptIn(FlowPreview::class)
    private fun observeAndAutosave() {
        autosaveJob = viewModelScope.launch {
            combine(_sudokuGame, _time) { game, time ->
                game?.copy(timeSpent = time)
            }.debounce(1000)
                .filterNotNull()
                .collect { game ->
                    sudokuRepository.insertSudoku(game)
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            _sudokuGame.value?.let { game ->
                sudokuRepository.insertSudoku(game.copy(timeSpent = _time.value))
            }
        }
    }
}