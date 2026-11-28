package com.example.wordie.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordie.data.AuthRepository
import com.example.wordie.data.LessonRepository
import com.example.wordie.data.getSampleLessons
import com.example.wordie.models.AppUser
import com.example.wordie.models.Lesson
import com.example.wordie.utils.getDeviceDefaultLanguage
import com.example.wordie.utils.isNetworkAvailable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val lessons: List<Lesson> = getSampleLessons(),
    val currentUser: AppUser? = null,
    val playerName: String = "TOMMY",
    val playerLevel: String = "Beginner",
    val stars: Int = 0,
    val lessonsDone: Int = 0,
    val isPremium: Boolean = false,
    val isDarkMode: Boolean = false,
    val nativeLanguage: String = "Vietnamese",
    val selectedTab: String = "home",
    val showMenu: Boolean = false,
    val showPremiumSheet: Boolean = false,
    val showLessonModeSheet: Boolean = false,
    val showLanguageSheet: Boolean = false,
    val selectedLesson: Lesson? = null,
    val isLoading: Boolean = false
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepo = AuthRepository()
    private val lessonRepo = LessonRepository()

    private val _uiState = MutableStateFlow(HomeUiState(nativeLanguage = getDeviceDefaultLanguage()))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // 1. Load Current User
            val savedUser = authRepo.getCurrentUser()
            if (savedUser != null) {
                _uiState.update { state ->
                    state.copy(
                        currentUser = savedUser,
                        playerName = savedUser.name.ifEmpty { "TOMMY" },
                        playerLevel = savedUser.level,
                        isPremium = savedUser.isPremium,
                        stars = savedUser.stars,
                        lessonsDone = savedUser.lessonsDone,
                        nativeLanguage = savedUser.nativeLanguage.ifEmpty { getDeviceDefaultLanguage() }
                    )
                }
            }

            // 2. Load Lessons
            if (isNetworkAvailable(getApplication())) {
                val result = lessonRepo.uploadLessonsToFirebase()
                result.onSuccess { count ->
                    if (count > 0) android.util.Log.d("WORDIE", "Đồng bộ $count bài học lên Firebase!")
                }
                val remoteLessons = lessonRepo.getLessons()
                if (remoteLessons.isNotEmpty()) {
                    _uiState.update { it.copy(lessons = remoteLessons) }
                }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun setNativeLanguage(lang: String) {
        _uiState.update { it.copy(nativeLanguage = lang, showLanguageSheet = false) }
        val uid = _uiState.value.currentUser?.uid ?: return
        viewModelScope.launch {
            authRepo.updateOnboardingInfo(uid, nativeLanguage = lang)
        }
    }

    fun toggleDarkMode(isDark: Boolean) {
        _uiState.update { it.copy(isDarkMode = isDark) }
    }

    fun selectTab(tab: String) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun toggleMenu(show: Boolean) {
        _uiState.update { it.copy(showMenu = show) }
    }

    fun togglePremiumSheet(show: Boolean) {
        _uiState.update { it.copy(showPremiumSheet = show) }
    }

    fun toggleLessonModeSheet(show: Boolean) {
        _uiState.update { it.copy(showLessonModeSheet = show) }
    }

    fun toggleLanguageSheet(show: Boolean) {
        _uiState.update { it.copy(showLanguageSheet = show) }
    }

    fun selectLesson(lesson: Lesson?) {
        _uiState.update { it.copy(selectedLesson = lesson) }
    }

    fun buyPremium() {
        _uiState.update { it.copy(isPremium = true, showPremiumSheet = false) }
        val uid = _uiState.value.currentUser?.uid ?: return
        viewModelScope.launch {
            authRepo.updatePremium(uid, true)
        }
    }

    fun signOut() {
        authRepo.signOut()
        _uiState.update { it.copy(currentUser = null, isPremium = false) }
    }

    fun completeLesson(starsEarned: Int = 1) {
        val user = _uiState.value.currentUser ?: return
        viewModelScope.launch {
            val updatedUser = authRepo.completeLesson(user.uid, user, starsEarned)
            _uiState.update { state ->
                state.copy(
                    currentUser = updatedUser,
                    stars = updatedUser.stars,
                    lessonsDone = updatedUser.lessonsDone
                )
            }
        }
    }
}
