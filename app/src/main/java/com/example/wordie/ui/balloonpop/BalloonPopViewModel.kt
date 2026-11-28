package com.example.wordie.ui.balloonpop

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordie.data.getSampleLessons
import com.example.wordie.models.Lesson
import com.example.wordie.models.WordItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

data class BalloonItemData(
    val id: Int,
    val word: WordItem,
    val isCorrect: Boolean,
    val xFraction: Float,
    val color: Color,
    val isPopped: Boolean = false
)

data class BalloonPopUiState(
    val timeLeft: Int = 30,
    val isGameOver: Boolean = false,
    val score: Int = 0,
    val poppedCount: Int = 0,
    val targetWord: WordItem = WordItem("dog", "🐶", "Dog", "Con chó"),
    val balloons: List<BalloonItemData> = emptyList(),
    val floatProgress: Float = 1.1f
)

class BalloonPopViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(BalloonPopUiState())
    val uiState: StateFlow<BalloonPopUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var floatJob: Job? = null

    private val balloonColors = listOf(
        Color(0xFFFF5252),
        Color(0xFF29B6F6),
        Color(0xFF66BB6A),
        Color(0xFFFF9800),
        Color(0xFFAB47BC),
        Color(0xFFEC407A)
    )

    private var allWords: List<WordItem> = emptyList()

    fun initGame(lesson: Lesson) {
        val list = lesson.words.toMutableList()
        if (list.size < 3) {
            val sampleWords = getSampleLessons().flatMap { it.words }
            sampleWords.forEach { w ->
                if (list.none { it.english.equals(w.english, ignoreCase = true) }) {
                    list.add(w)
                }
            }
        }
        allWords = list
        resetGame()
    }

    fun resetGame() {
        timerJob?.cancel()
        floatJob?.cancel()

        _uiState.update {
            BalloonPopUiState(
                timeLeft = 30,
                isGameOver = false,
                score = 0,
                poppedCount = 0
            )
        }

        spawnNewWave()
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.timeLeft > 0 && !_uiState.value.isGameOver) {
                delay(1000L)
                _uiState.update { state ->
                    val newTime = state.timeLeft - 1
                    state.copy(
                        timeLeft = newTime,
                        isGameOver = newTime <= 0
                    )
                }
            }
        }
    }

    fun spawnNewWave() {
        if (allWords.isEmpty()) return
        val newTarget = allWords.random()

        val distractors = allWords.filter { !it.english.equals(newTarget.english, ignoreCase = true) }
            .shuffled()
            .take(2)

        val waveWords = (listOf(newTarget) + distractors).shuffled()
        val xPositions = listOf(0.20f, 0.50f, 0.80f).shuffled()
        val availableColors = balloonColors.shuffled()

        val newBalloons = waveWords.mapIndexed { index, word ->
            BalloonItemData(
                id = Random.nextInt(),
                word = word,
                isCorrect = word.english.equals(newTarget.english, ignoreCase = true),
                xFraction = xPositions[index],
                color = availableColors[index % availableColors.size],
                isPopped = false
            )
        }

        _uiState.update {
            it.copy(
                targetWord = newTarget,
                balloons = newBalloons,
                floatProgress = 1.1f
            )
        }

        startFloatLoop()
    }

    private fun startFloatLoop() {
        floatJob?.cancel()
        floatJob = viewModelScope.launch {
            val durationMs = 4500L
            val startTime = System.currentTimeMillis()

            while (_uiState.value.floatProgress > -0.2f && !_uiState.value.isGameOver) {
                val elapsed = System.currentTimeMillis() - startTime
                val progress = 1.1f - (1.3f * (elapsed.toFloat() / durationMs))

                _uiState.update { it.copy(floatProgress = progress) }
                delay(16L) // ~60 FPS smooth float
            }

            // Missed wave (floated past top)
            if (_uiState.value.floatProgress <= -0.2f && !_uiState.value.isGameOver) {
                _uiState.update { it.copy(score = (it.score - 5).coerceAtLeast(0)) }
                spawnNewWave()
            }
        }
    }

    fun popBalloon(balloon: BalloonItemData, onSpeakClick: (String) -> Unit) {
        if (_uiState.value.isGameOver) return

        if (balloon.isCorrect) {
            onSpeakClick(balloon.word.english)
            _uiState.update {
                it.copy(
                    score = it.score + 10,
                    poppedCount = it.poppedCount + 1
                )
            }
            spawnNewWave()
        } else {
            _uiState.update { state ->
                val updatedBalloons = state.balloons.map {
                    if (it.id == balloon.id) it.copy(isPopped = true) else it
                }
                state.copy(
                    score = (state.score - 5).coerceAtLeast(0),
                    balloons = updatedBalloons
                )
            }
        }
    }

    override fun onCleared() {
        timerJob?.cancel()
        floatJob?.cancel()
        super.onCleared()
    }
}
