package com.example.alzapp.ui.screens.games.postman.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alzapp.data.user.AchievementRepository
import com.example.alzapp.data.user.UserStatsRepository
import com.example.alzapp.ui.screens.games.postman.HouseColor
import com.example.alzapp.ui.screens.games.postman.PostmanTutorialStep
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class PostmanViewModel @Inject constructor(
    private val userStatsRepository: UserStatsRepository,
    private val achievementRepository: AchievementRepository
) : ViewModel() {

    // n steps back that user needs to memorize (houses' colors)
    private val _n = MutableStateFlow(1)
    val n = _n.asStateFlow()

    private val _currentHouseColor = MutableStateFlow<HouseColor?>(null)
    val currentHouseColor = _currentHouseColor.asStateFlow()

    // history of colors
    private val _history = MutableStateFlow<List<HouseColor>>(emptyList())
    val history = _history.asStateFlow()

    // letters delivered correctly by user
    private val _lettersDelivered = MutableStateFlow(0)
    val lettersDelivered = _lettersDelivered.asStateFlow()

    // all possible matches in one game
    private val _totalDeliverable = MutableStateFlow(0)
    val totalDeliverable = _totalDeliverable.asStateFlow()

    // count of houses that player already visited
    private val _totalTrials = MutableStateFlow(0)

    private val _feedbackMessage = MutableStateFlow<String?>(null)
    val feedbackMessage = _feedbackMessage.asStateFlow()

    private val _showLetterAnimation = MutableStateFlow(false)
    val showLetterAnimation = _showLetterAnimation.asStateFlow()

    private val _isGameFinished = MutableStateFlow(false)
    val isGameFinished = _isGameFinished.asStateFlow()

    // road progress
    private val _progress = MutableStateFlow(0f)
    val progress = _progress.asStateFlow()

    private val _tutorialStep = MutableStateFlow(PostmanTutorialStep.NONE)
    val tutorialStep = _tutorialStep.asStateFlow()

    private val maxTrials = 15 // 15 houses on each game's postman 'route'

    init {
        generateNextHouse()
    }

    fun startTutorial() {
        _tutorialStep.value = PostmanTutorialStep.WELCOME
        _currentHouseColor.value = HouseColor.GREEN
        _history.value = listOf(HouseColor.GREEN)
        _n.value = 1
        _lettersDelivered.value = 0
        _totalDeliverable.value = 0
        _totalTrials.value = 0
        _feedbackMessage.value = null
        _showLetterAnimation.value = false
        _isGameFinished.value = false
        _progress.value = 0f
    }

    fun nextTutorialStep() {
        when (_tutorialStep.value) {
            PostmanTutorialStep.WELCOME -> _tutorialStep.value = PostmanTutorialStep.SHOW_FIRST
            PostmanTutorialStep.SHOW_FIRST -> {
                // force a match for 1-back tutorial: show another green house
                _tutorialStep.value = PostmanTutorialStep.SHOW_MATCH
                _currentHouseColor.value = HouseColor.GREEN
                _history.value += HouseColor.GREEN
                _totalDeliverable.value = 1
            }
            PostmanTutorialStep.SUCCESS_HIT -> _tutorialStep.value = PostmanTutorialStep.COMPLETED
            PostmanTutorialStep.COMPLETED -> {
                startGame(_n.value)
                _tutorialStep.value = PostmanTutorialStep.NONE
            }
            else -> {}
        }
    }

    fun startGame(n: Int) {
        _n.value = n
        _tutorialStep.value = PostmanTutorialStep.NONE
        _currentHouseColor.value = null
        _history.value = emptyList()
        _lettersDelivered.value = 0
        _totalDeliverable.value = 0
        _totalTrials.value = 0
        _feedbackMessage.value = null
        _showLetterAnimation.value = false
        _isGameFinished.value = false
        _progress.value = 0f
        generateNextHouse()
    }

    // handling next house on postman's route
    fun onNextClick() {
        if (_tutorialStep.value != PostmanTutorialStep.NONE) {
            if (_tutorialStep.value == PostmanTutorialStep.SHOW_FIRST || _tutorialStep.value == PostmanTutorialStep.SUCCESS_HIT) {
                nextTutorialStep()
            }
            return
        }

        if (_isGameFinished.value) return

        if (_totalTrials.value >= maxTrials) {
            finishGame()
        } else {
            _feedbackMessage.value = null
            _showLetterAnimation.value = false
            generateNextHouse()
        }
    }

    fun onMatchClick() {
        // tutorial Logic
        if (_tutorialStep.value == PostmanTutorialStep.SHOW_MATCH) {
            _tutorialStep.value = PostmanTutorialStep.SUCCESS_HIT
            _lettersDelivered.value = 1
            _showLetterAnimation.value = true
            _feedbackMessage.value = "Brawo! List dostarczony!"
            return
        }

        if (_feedbackMessage.value != null || _isGameFinished.value || _tutorialStep.value != PostmanTutorialStep.NONE) return

        // verify if current house is of the same color as the one n steps back
        if (_history.value.size > _n.value) {
            val current = _currentHouseColor.value
            val target = _history.value[_history.value.size - 1 - _n.value]

            // it's a match
            if (current == target) {
                _lettersDelivered.value += 1
                _feedbackMessage.value = "Brawo! List dostarczony!"
                _showLetterAnimation.value = true
                // unlock "Dobry Sąsiad" achievement
                viewModelScope.launch {
                    achievementRepository.unlockAchievement("postman_1")
                }
            } else {
                // feedback message explaining why the given answer is wrong
                val stepWord = if (_n.value == 1) "kroku" else "kroków"
                _feedbackMessage.value = "Ten dom jest ${current?.displayName}, a dom sprzed ${_n.value} $stepWord był ${target.displayName}."
            }
        }
    }

    private fun generateNextHouse() {
        val colors = HouseColor.entries
        val historyVal = _history.value
        val nVal = _n.value

        val targetColor = if (historyVal.size >= nVal) historyVal[historyVal.size - nVal] else null

        // about 30% chances of a match
        val isMatch = targetColor != null && Random.nextFloat() < 0.3f
        val nextColor = if (isMatch) targetColor else colors.random()
        
        _currentHouseColor.value = nextColor
        _history.value = historyVal + nextColor
        _totalTrials.value += 1
        // how many matches were generated (for end-game pop-up)
        if (nextColor == targetColor) {
            _totalDeliverable.value += 1
        }
        // progress bar is updated
        _progress.value = (_totalTrials.value).toFloat() / maxTrials
    }

    private fun finishGame() {
        _isGameFinished.value = true
        viewModelScope.launch {
            userStatsRepository.updateStreak()
        }
    }
}
