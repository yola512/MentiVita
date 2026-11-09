package com.example.alzapp.ui.screens.games.cryptogram.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alzapp.data.games.cryptogram.CryptogramRepository
import com.example.alzapp.data.user.AchievementRepository
import com.example.alzapp.data.games.cryptogram.local.ProverbEntity
import com.example.alzapp.data.user.UserStatsRepository
import com.example.alzapp.ui.screens.games.cryptogram.CryptogramTutorialStep
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

val POLISH_ALPHABET = (('A'..'Z').toList() - listOf('Q', 'V', 'X')) +
        listOf('Ą', 'Ć', 'Ę', 'Ł', 'Ń', 'Ó', 'Ś', 'Ź', 'Ż')

@HiltViewModel
class CryptogramViewModel @Inject constructor(
    private val cryptogramRepository: CryptogramRepository,
    private val userStatsRepository: UserStatsRepository,
    private val achievementRepository: AchievementRepository
) : ViewModel() {

    private val _proverb = MutableStateFlow("")
    val proverb = _proverb.asStateFlow()

    private val _category = MutableStateFlow("")
    val category = _category.asStateFlow()

    // map storing a cipher; assigns letter to number
    private val _cipherMap = MutableStateFlow<Map<Char, Int>>(emptyMap())
    val cipherMap = _cipherMap.asStateFlow()

    // user guesses; assigns number to letter
    private val _userGuesses = MutableStateFlow<Map<Int, Char?>>(emptyMap())
    val userGuesses = _userGuesses.asStateFlow()

    private val _keyboardLetters = MutableStateFlow<List<Char>>(emptyList())
    val keyboardLetters = _keyboardLetters.asStateFlow()

    // letters of the cipher guessed correctly
    private val _completedLetters = MutableStateFlow<Set<Char>>(emptySet())
    val completedLetters = _completedLetters.asStateFlow()

    private val _isSolved = MutableStateFlow(false)
    val isSolved = _isSolved.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _tutorialStep = MutableStateFlow(CryptogramTutorialStep.NONE)
    val tutorialStep = _tutorialStep.asStateFlow()

    private val _highlightCipher = MutableStateFlow<Int?>(null)
    val highlightCipher = _highlightCipher.asStateFlow()

    private val _errorEvent = MutableSharedFlow<Int>(replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val errorEvent = _errorEvent.asSharedFlow()

    private val _successEvent = MutableSharedFlow<Int>(replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val successEvent = _successEvent.asSharedFlow()

    private var currentProverbEntity: ProverbEntity? = null

    init {
        loadGame()
    }

    // interactive tutorial with a simple word "SOSNA"
    fun startTutorial() {
        viewModelScope.launch {
            _isLoading.value = true
            // "SOSNA" has repeating 'S' (1) to show that one guess fills multiple boxes
            val tutorialProverb = "SOSNA"
            val cipherMap = mapOf('S' to 1, 'O' to 2, 'N' to 3, 'A' to 4)
            val initialGuesses = mapOf(1 to null, 2 to null, 3 to null, 4 to null)
            val keyboard = listOf('S', 'O', 'N', 'A', 'B')

            _proverb.value = tutorialProverb
            _category.value = "Samouczek"
            _cipherMap.value = cipherMap
            _userGuesses.value = initialGuesses
            _keyboardLetters.value = keyboard
            _completedLetters.value = emptySet()
            _isLoading.value = false
            _tutorialStep.value = CryptogramTutorialStep.WELCOME
            _isSolved.value = false
            _highlightCipher.value = null
        }
    }

    // handling tutorial steps
    fun nextTutorialStep() {
        when (_tutorialStep.value) {
            CryptogramTutorialStep.WELCOME -> _tutorialStep.value = CryptogramTutorialStep.EXPLAIN_TILES
            CryptogramTutorialStep.EXPLAIN_TILES -> _tutorialStep.value = CryptogramTutorialStep.EXPLAIN_KEYBOARD
            CryptogramTutorialStep.EXPLAIN_KEYBOARD -> {
                _tutorialStep.value = CryptogramTutorialStep.GUESS_LETTER
                _highlightCipher.value = 1
            }
            CryptogramTutorialStep.EXPLAIN_KEYBOARD_DISABLED -> _tutorialStep.value = CryptogramTutorialStep.EXPLAIN_HINT
            CryptogramTutorialStep.EXPLAIN_HINT -> _tutorialStep.value = CryptogramTutorialStep.SUCCESS
            CryptogramTutorialStep.SUCCESS -> _tutorialStep.value = CryptogramTutorialStep.COMPLETED
            CryptogramTutorialStep.COMPLETED -> {
                loadGame() // start real game
                _tutorialStep.value = CryptogramTutorialStep.NONE
            }
            else -> {}
        }
    }

    private fun loadGame() {
        viewModelScope.launch {
            _isLoading.value = true
            _isSolved.value = false
            _tutorialStep.value = CryptogramTutorialStep.NONE

            // resume unfinished game or start new game with random proverb
            var proverbEntity = cryptogramRepository.getUnfinishedProverb()
            if (proverbEntity == null) {
                proverbEntity = cryptogramRepository.getRandomNewProverb()
            }

            if (proverbEntity != null) {
                currentProverbEntity = proverbEntity

                if (proverbEntity.cipherMap != null && proverbEntity.userGuesses != null) {
                    try {
                        val savedCipherMap = Json.decodeFromString<Map<String, Int>>(proverbEntity.cipherMap)
                            .mapKeys { it.key[0] }
                        val savedUserGuesses = Json.decodeFromString<Map<Int, Char?>>(proverbEntity.userGuesses)

                        val content = proverbEntity.content.uppercase()
                        val keyboard = generateKeyboardLetters(content)

                        _proverb.value = content
                        _category.value = proverbEntity.category
                        _cipherMap.value = savedCipherMap
                        _userGuesses.value = savedUserGuesses
                        _keyboardLetters.value = keyboard
                        _completedLetters.value = calculateCompletedLetters(content, savedCipherMap, savedUserGuesses)
                        _isSolved.value = proverbEntity.isCompleted
                        _isLoading.value = false
                    } catch (e: Exception) {
                        setupGame(proverbEntity.content.uppercase(), proverbEntity.category)
                    }
                } else {
                    setupGame(proverbEntity.content.uppercase(), proverbEntity.category)
                }
            } else {
                _proverb.value = ""
                _isLoading.value = false
            }
        }
    }

    fun loadNewGame() {
        viewModelScope.launch {
            _isLoading.value = true
            _isSolved.value = false
            val proverbEntity = cryptogramRepository.getRandomNewProverb()
            if (proverbEntity != null) {
                currentProverbEntity = proverbEntity
                setupGame(proverbEntity.content.uppercase(), proverbEntity.category)
            } else {
                _isLoading.value = false
            }
        }
    }

    // creates unique cipher for proverb
    private fun setupGame(content: String, category: String) {
        val uniqueLetters = content.filter { it.isLetter() }.toSet().toList()
        val shuffledNumbers = (1..uniqueLetters.size).shuffled()

        val cipherMap = uniqueLetters.zip(shuffledNumbers).toMap()
        val initialGuesses = shuffledNumbers.associateWith { null as Char? }
        val keyboard = generateKeyboardLetters(content)

        _proverb.value = content
        _category.value = category
        _cipherMap.value = cipherMap
        _userGuesses.value = initialGuesses
        _keyboardLetters.value = keyboard
        _completedLetters.value = emptySet()
        _isSolved.value = false
        _isLoading.value = false

        saveProgress()
    }

    private fun generateKeyboardLetters(proverb: String): List<Char> {
        val needed = proverb.filter { it.isLetter() }.toSet().toList()
        val remaining = POLISH_ALPHABET.filterNot { it in needed }
        val decoys = remaining.shuffled().take(5)
        return (needed + decoys).shuffled()
    }

    // if user correctly guesses letter to field, the letter will be added to completed list
    // and thanks to it ui knows what letters are no longer needed to guess the proverb
    private fun calculateCompletedLetters(
        proverb: String,
        cipherMap: Map<Char, Int>,
        userGuesses: Map<Int, Char?>
    ): Set<Char> {
        val needed = proverb.filter { it.isLetter() }.toSet()
        return needed.filter { char ->
            val cipherNo = cipherMap[char]
            userGuesses[cipherNo] == char
        }.toSet()
    }

    // called when user selects a letter from the keyboard for selected field
    fun onGuessChange(cipherNumber: Int, guess: Char) {
        viewModelScope.launch {
            val correctChar = _cipherMap.value.entries.find { it.value == cipherNumber }?.key

            if (guess != correctChar) {
                _errorEvent.emit(cipherNumber) // haptic/visual feedback
                return@launch
            }

            _successEvent.emit(cipherNumber)

            val newGuesses = _userGuesses.value.toMutableMap()
            newGuesses[cipherNumber] = guess
            _userGuesses.value = newGuesses

            val solved = checkWin()

            // streaks is updated and achievement is unlocked if solved
            if (solved && !_isSolved.value) {
                userStatsRepository.updateStreak()
                achievementRepository.unlockAchievement("proverbs_1")
            }

            _isSolved.value = solved
            _completedLetters.value = calculateCompletedLetters(_proverb.value, _cipherMap.value, newGuesses)

            if (_tutorialStep.value == CryptogramTutorialStep.GUESS_LETTER) {
                _tutorialStep.value = CryptogramTutorialStep.EXPLAIN_KEYBOARD_DISABLED
                _highlightCipher.value = null
            }

            if (_tutorialStep.value == CryptogramTutorialStep.NONE) {
                saveProgress()
            }
        }
    }

    private fun checkWin(): Boolean {
        if (_proverb.value.isEmpty()) return false
        for ((original, cipherNumber) in _cipherMap.value) {
            if (_userGuesses.value[cipherNumber] != original) return false
        }
        return true
    }

    // saves game progress
    private fun saveProgress() {
        viewModelScope.launch {
            currentProverbEntity?.let { entity ->
                val updatedEntity = entity.copy(
                    isCompleted = _isSolved.value,
                    cipherMap = Json.encodeToString(_cipherMap.value.mapKeys { it.key.toString() }),
                    userGuesses = Json.encodeToString(_userGuesses.value)
                )
                cryptogramRepository.updateProverb(updatedEntity)
                currentProverbEntity = updatedEntity
            }
        }
    }

    // hint button related; automatically inserts correct letter into selected field
    fun giveHint(targetCipherNumber: Int?) {
        if (targetCipherNumber != null) {
            val originalEntry = _cipherMap.value.entries.find { it.value == targetCipherNumber }
            originalEntry?.let {
                onGuessChange(targetCipherNumber, it.key)
            }
        }
    }
}
