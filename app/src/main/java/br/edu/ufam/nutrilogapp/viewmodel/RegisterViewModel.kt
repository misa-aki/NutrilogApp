package br.edu.ufam.nutrilogapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ufam.nutrilogapp.data.auth.AuthManager
import br.edu.ufam.nutrilogapp.data.model.RegisterRequest
import br.edu.ufam.nutrilogapp.data.service.RegisterService
import br.edu.ufam.nutrilogapp.data.service.RegisterServiceImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RegisterUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRegisterSuccessful: Boolean = false
)

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val authManager = AuthManager(application.applicationContext)
    private val registerService: RegisterService = RegisterServiceImpl(authManager)

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun register(username: String, email: String, password: String, onSuccess: () -> Unit) {
        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Por favor, preencha todos os campos"
            )
            return
        }

        if (password.length < 6) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "A senha deve ter pelo menos 6 caracteres"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            val request = RegisterRequest(
                username = username,
                email = email,
                password = password
            )

            registerService.register(request)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isRegisterSuccessful = true,
                        errorMessage = null
                    )
                    onSuccess()
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isRegisterSuccessful = false,
                        errorMessage = exception.message ?: "Erro ao criar conta"
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

