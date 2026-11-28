package com.example.wordie.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordie.data.AuthRepository
import com.example.wordie.models.AppUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val currentUser: AppUser? = null,
    val isSuccess: Boolean = false
)

class AuthViewModel : ViewModel() {
    private val authRepo = AuthRepository()

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun signIn(email: String, password: String, onSuccess: (AppUser) -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = authRepo.signIn(email, password)
            result.onSuccess { user ->
                _uiState.update { it.copy(isLoading = false, currentUser = user, isSuccess = true) }
                onSuccess(user)
            }.onFailure { e ->
                val msg = parseAuthError(e)
                _uiState.update { it.copy(isLoading = false, errorMessage = msg) }
            }
        }
    }

    fun signUp(name: String, email: String, password: String, onSuccess: (AppUser) -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = authRepo.signUp(name, email, password)
            result.onSuccess { user ->
                _uiState.update { it.copy(isLoading = false, currentUser = user, isSuccess = true) }
                onSuccess(user)
            }.onFailure { e ->
                val msg = parseAuthError(e)
                _uiState.update { it.copy(isLoading = false, errorMessage = msg) }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun parseAuthError(e: Throwable): String {
        val msg = e.message ?: ""
        return when {
            msg.contains("credential", ignoreCase = true) ||
            msg.contains("password", ignoreCase = true) ||
            msg.contains("user-not-found", ignoreCase = true) ||
            msg.contains("invalid-credential", ignoreCase = true) ->
                "Email hoặc Mật khẩu không chính xác!"
            msg.contains("email-already-in-use", ignoreCase = true) ->
                "Email này đã được đăng ký tài khoản!"
            msg.contains("weak-password", ignoreCase = true) ->
                "Mật khẩu quá yếu! Cần ít nhất 6 ký tự."
            msg.contains("invalid-email", ignoreCase = true) ->
                "Định dạng Email không hợp lệ!"
            msg.contains("network", ignoreCase = true) ->
                "Lỗi kết nối mạng! Vui lòng kiểm tra lại Wifi/Internet."
            msg.contains("too-many-requests", ignoreCase = true) ->
                "Bạn đã thử quá nhiều lần. Vui lòng đợi ít phút rồi thử lại!"
            msg.isNotBlank() -> msg
            else -> "Đăng nhập/Đăng ký thất bại. Vui lòng thử lại!"
        }
    }
}
