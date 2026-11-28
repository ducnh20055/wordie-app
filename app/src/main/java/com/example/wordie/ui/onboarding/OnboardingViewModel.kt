package com.example.wordie.ui.onboarding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordie.data.AuthRepository
import com.example.wordie.utils.getDeviceDefaultLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OnboardingUiState(
    val name: String = "",
    val age: Int = 10,
    val nativeLanguage: String = getDeviceDefaultLanguage(),
    val learningReason: String = "Travel & Culture",
    val targetTime: String = "1 Month",
    val level: String = "Beginner",
    val nameError: String? = null,
    val ageError: String? = null
)

class OnboardingViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepo = AuthRepository()

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun updateNameAndAge(name: String, age: Int) {
        _uiState.update { it.copy(name = name, age = age) }
        viewModelScope.launch {
            val user = authRepo.getCurrentUser()
            user?.let {
                authRepo.updateUserInfo(it.uid, name, age)
            }
        }
    }

    fun updateNativeLanguage(language: String) {
        _uiState.update { it.copy(nativeLanguage = language) }
        viewModelScope.launch {
            val user = authRepo.getCurrentUser()
            user?.let {
                authRepo.updateOnboardingInfo(it.uid, nativeLanguage = language)
            }
        }
    }

    fun updateReason(reason: String) {
        _uiState.update { it.copy(learningReason = reason) }
        viewModelScope.launch {
            val user = authRepo.getCurrentUser()
            user?.let {
                authRepo.updateOnboardingInfo(it.uid, learningReason = reason)
            }
        }
    }

    fun updateTargetTime(target: String) {
        _uiState.update { it.copy(targetTime = target) }
        viewModelScope.launch {
            val user = authRepo.getCurrentUser()
            user?.let {
                authRepo.updateOnboardingInfo(it.uid, targetTime = target)
            }
        }
    }

    fun updateLevel(level: String) {
        _uiState.update { it.copy(level = level) }
        viewModelScope.launch {
            val user = authRepo.getCurrentUser()
            user?.let {
                authRepo.updateLevel(it.uid, level)
            }
        }
    }
}
