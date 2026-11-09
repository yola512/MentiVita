package com.example.alzapp.ui.screens.games.hiddenobject.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alzapp.R
import com.example.alzapp.data.games.hiddenObjects.local.HiddenObjectEntity
import com.example.alzapp.data.games.hiddenObjects.HiddenObjectRepository
import com.example.alzapp.data.games.hiddenObjects.model.HiddenObject
import com.example.alzapp.data.games.hiddenObjects.model.HiddenObjectLevel
import com.example.alzapp.data.user.UserStatsRepository
import com.example.alzapp.ui.screens.games.hiddenobject.HiddenObjectTutorialStep
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.pow
import kotlin.math.sqrt

@HiltViewModel
class HiddenObjectViewModel @Inject constructor(
    private val repository: HiddenObjectRepository,
    private val userStatsRepository: UserStatsRepository
) : ViewModel() {

    // current game session id; created in order not to ask database about the id every time the game is saved
    private var currentGameId: Long = 0L
    private val level1 = HiddenObjectLevel(
        id = 1,
        imageResId = R.drawable.hidden_objects1,
        objectsToFind = listOf(
            HiddenObject(1, "Niebieska konewka", 57f, 92f, 5f),
            HiddenObject(2, "Łopata", 45f, 60f, 6f),
            HiddenObject(3, "Krasnal", 93f, 57f, 5f),
            HiddenObject(4, "Czerwony but", 42f, 59f, 3f),
            HiddenObject(5, "Wąż ogrodowy", 12f, 40f, 6f),
            HiddenObject(6, "Niebieska spódnica", 28f, 70f, 6f),
            HiddenObject(7, "Okno", 90f, 32f, 8f)
        )
    )

    // state flows
    private val _level = MutableStateFlow(level1)
    val level: StateFlow<HiddenObjectLevel> = _level.asStateFlow()

    // objects found by user
    private val _foundObjectIds = MutableStateFlow<Set<Int>>(emptySet())
    val foundObjectIds: StateFlow<Set<Int>> = _foundObjectIds.asStateFlow()

    private val _isLevelComplete = MutableStateFlow(false)
    val isLevelComplete: StateFlow<Boolean> = _isLevelComplete.asStateFlow()

    private val _tutorialStep = MutableStateFlow(HiddenObjectTutorialStep.NONE)
    val tutorialStep: StateFlow<HiddenObjectTutorialStep> = _tutorialStep.asStateFlow()

    init {
        loadProgress()
    }

    // loads progress of an unfinished game
    private fun loadProgress() {
        viewModelScope.launch {
            val savedGame = repository.getUnfinishedGame()
            if (savedGame != null) {
                currentGameId = savedGame.id
                _foundObjectIds.value = savedGame.foundObjectIds
                _isLevelComplete.value = savedGame.isCompleted
            }
        }
    }

    // saves current game progress
    private fun saveProgress() {
        viewModelScope.launch {
            val entity = HiddenObjectEntity(
                id = currentGameId,
                imageResId = _level.value.imageResId,
                objectsToFind = _level.value.objectsToFind,
                foundObjectIds = _foundObjectIds.value,
                isCompleted = _isLevelComplete.value
            )
            val newId = repository.insertGame(entity)
            if (currentGameId == 0L) {
                currentGameId = newId
            }
        }
    }

    fun startTutorial() {
        _tutorialStep.value = HiddenObjectTutorialStep.WELCOME
        _foundObjectIds.value = emptySet()
        _isLevelComplete.value = false
    }

    // handles tutorial steps
    fun nextTutorialStep() {
        when (_tutorialStep.value) {
            HiddenObjectTutorialStep.WELCOME -> _tutorialStep.value = HiddenObjectTutorialStep.EXPLAIN_LIST
            HiddenObjectTutorialStep.EXPLAIN_LIST -> _tutorialStep.value = HiddenObjectTutorialStep.EXPLAIN_SEARCH
            HiddenObjectTutorialStep.EXPLAIN_SEARCH -> _tutorialStep.value = HiddenObjectTutorialStep.FIND_FIRST
            HiddenObjectTutorialStep.SUCCESS -> _tutorialStep.value = HiddenObjectTutorialStep.COMPLETED
            HiddenObjectTutorialStep.COMPLETED -> _tutorialStep.value = HiddenObjectTutorialStep.NONE
            else -> {}
        }
    }

    // handles click on the image; converts pixel coordinates to percents
    fun onImageClick(xPx: Float, yPx: Float, layoutWidth: Float, layoutHeight: Float) {
        if (_isLevelComplete.value) return

        // if in tutorial and not in FIND_FIRST step, ignore clicks on image
        if (_tutorialStep.value != HiddenObjectTutorialStep.NONE && 
            _tutorialStep.value != HiddenObjectTutorialStep.FIND_FIRST) return

        // search the list of objects in order to find the one that matches click location
        val objFound = _level.value.objectsToFind.find { obj ->
            // skip already found
            if (obj.id in _foundObjectIds.value) return@find false

            // in tutorial mode (FIND_FIRST), we only allow finding the first object (here - shovel)
            if (_tutorialStep.value == HiddenObjectTutorialStep.FIND_FIRST && obj.id != 2) return@find false

            val objXPx = (obj.xPercent / 100f) * layoutWidth
            val objYPx = (obj.yPercent / 100f) * layoutHeight

            // calculate click radius
            val minDimension = minOf(layoutWidth, layoutHeight)
            val radiusPx = (obj.radiusPercent / 100f) * minDimension
            val effectiveRadius = maxOf(radiusPx * 1.5f, 48f)

            val distance = sqrt((xPx - objXPx).pow(2) + (yPx - objYPx).pow(2))

            distance <= effectiveRadius
        }

        // if object was found
        if (objFound != null) {
            val newFoundIds = _foundObjectIds.value + objFound.id

            // if object was found in tutorial, go to tutorial success step
            if (_tutorialStep.value == HiddenObjectTutorialStep.FIND_FIRST) {
                _foundObjectIds.value = newFoundIds
                _tutorialStep.value = HiddenObjectTutorialStep.SUCCESS
            } else {
                // in normal gameplay - update list of found objects and check if the level is complete
                _foundObjectIds.value = newFoundIds
                val isComplete = newFoundIds.size == _level.value.objectsToFind.size
                _isLevelComplete.value = isComplete
                
                if (isComplete) {
                    viewModelScope.launch {
                        userStatsRepository.updateStreak()
                    }
                }
            }

            saveProgress() // save after each found object
        }
    }

    fun resetGame() {
        currentGameId = 0L
        _foundObjectIds.value = emptySet()
        _isLevelComplete.value = false
    }
}
