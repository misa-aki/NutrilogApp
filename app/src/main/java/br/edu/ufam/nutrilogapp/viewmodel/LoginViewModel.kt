package br.edu.ufam.nutrilogapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ufam.nutrilogapp.data.auth.AuthManager
import br.edu.ufam.nutrilogapp.data.service.AuthService
import br.edu.ufam.nutrilogapp.data.service.AuthServiceImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccessful: Boolean = false
)

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val authManager = AuthManager(application.applicationContext)
    private val authService: AuthService = AuthServiceImpl(authManager)

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(username: String, password: String, onSuccess: () -> Unit) {
        if (username.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Por favor, preencha todos os campos"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            authService.login(username, password)
                .onSuccess { loginResponse ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoginSuccessful = true,
                        errorMessage = null
                    )
                    onSuccess()
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoginSuccessful = false,
                        errorMessage = exception.message ?: "Erro ao fazer login"
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

