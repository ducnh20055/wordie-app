package com.example.wordie.ui.grammar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordie.models.GrammarChapter
import com.example.wordie.models.GrammarQuestion
import com.example.wordie.models.getSampleGrammarChapters
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GrammarUiState(
    val chapters: List<GrammarChapter> = getSampleGrammarChapters(),
    val currentChapterIndex: Int = 0,
    val currentQuestionIndex: Int = 0,
    val selectedWords: List<String> = emptyList(),
    val isAnswerCorrect: Boolean? = null,
    val elapsedTimeSecs: Int = 0,
    val accuracyPercent: Int = 100,
    val isLessonFinished: Boolean = false,
    val totalLessonsDone: Int = 0
) {
    val currentChapter: GrammarChapter
        get() = chapters.getOrElse(currentChapterIndex) { getSampleGrammarChapters().first() }
}

class GrammarViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GrammarUiState())
    val uiState: StateFlow<GrammarUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    val currentChapter: GrammarChapter
        get() = _uiState.value.chapters.getOrElse(_uiState.value.currentChapterIndex) {
            getSampleGrammarChapters().first()
        }

    val currentQuestion: GrammarQuestion?
        get() = currentChapter.questions.getOrNull(_uiState.value.currentQuestionIndex)

    fun selectChapterIndex(index: Int) {
        if (index in _uiState.value.chapters.indices) {
            _uiState.update { it.copy(currentChapterIndex = index) }
        }
    }

    fun startLesson(chapter: GrammarChapter) {
        val chapterIdx = _uiState.value.chapters.indexOfFirst { it.id == chapter.id }.coerceAtLeast(0)
        val question = chapter.questions.firstOrNull()
        val initialWords = question?.wordOptions?.shuffled() ?: emptyList()

        _uiState.update {
            it.copy(
                currentChapterIndex = chapterIdx,
                currentQuestionIndex = 0,
                selectedWords = initialWords,
                isAnswerCorrect = null,
                elapsedTimeSecs = 0,
                accuracyPercent = 100,
                isLessonFinished = false
            )
        }

        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (!_uiState.value.isLessonFinished) {
                delay(1000L)
                _uiState.update { it.copy(elapsedTimeSecs = it.elapsedTimeSecs + 1) }
            }
        }
    }

    fun swapWords(fromIndex: Int, toIndex: Int) {
        val words = _uiState.value.selectedWords.toMutableList()
        if (fromIndex in words.indices && toIndex in words.indices) {
            val temp = words[fromIndex]
            words[fromIndex] = words[toIndex]
            words[toIndex] = temp
            _uiState.update { it.copy(selectedWords = words) }
        }
    }

    fun checkAnswer(onResult: (Boolean) -> Unit) {
        val question = currentQuestion ?: return
        val userChoice = _uiState.value.selectedWords
        val isCorrect = userChoice == question.correctWords

        _uiState.update { it.copy(isAnswerCorrect = isCorrect) }

        if (isCorrect) {
            onResult(true)
        } else {
            _uiState.update { it.copy(accuracyPercent = (it.accuracyPercent - 10).coerceAtLeast(50)) }
            onResult(false)
        }
    }

    fun nextQuestionOrFinish() {
        val chapter = currentChapter
        val nextQuestionIdx = _uiState.value.currentQuestionIndex + 1

        if (nextQuestionIdx < chapter.questions.size) {
            val nextQuestion = chapter.questions[nextQuestionIdx]
            _uiState.update {
                it.copy(
                    currentQuestionIndex = nextQuestionIdx,
                    selectedWords = nextQuestion.wordOptions.shuffled(),
                    isAnswerCorrect = null
                )
            }
        } else {
            // Lesson Finished! Update Chapter Stars and Progress
            timerJob?.cancel()
            val newTotalDone = _uiState.value.totalLessonsDone + 1
            val updatedChapters = _uiState.value.chapters.mapIndexed { idx, ch ->
                if (idx == _uiState.value.currentChapterIndex) {
                    val newStars = (ch.starsEarned + 1).coerceAtMost(ch.totalStars)
                    val newProgress = ((newStars.toFloat() / ch.totalStars.toFloat()) * 100).toInt()
                    ch.copy(
                        starsEarned = newStars,
                        progressPercent = newProgress,
                        streakDays = (ch.streakDays + 1)
                    )
                } else ch
            }

            _uiState.update {
                it.copy(
                    chapters = updatedChapters,
                    isLessonFinished = true,
                    totalLessonsDone = newTotalDone
                )
            }
        }
    }

    override fun onCleared() {
        timerJob?.cancel()
        super.onCleared()
    }
}
