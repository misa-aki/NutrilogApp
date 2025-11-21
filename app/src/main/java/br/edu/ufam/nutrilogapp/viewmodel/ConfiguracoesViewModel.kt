package br.edu.ufam.nutrilogapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ufam.nutrilogapp.data.model.UserGoal
import br.edu.ufam.nutrilogapp.data.model.UserProfile
import br.edu.ufam.nutrilogapp.data.service.ProfileService
import br.edu.ufam.nutrilogapp.data.service.ProfileServiceImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ConfiguracoesUiState(
    val userProfile: UserProfile? = null,
    val userGoal: UserGoal? = null,
    val isLoading: Boolean = true
)

class ConfiguracoesViewModel(
    private val profileService: ProfileService = ProfileServiceImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConfiguracoesUiState())
    val uiState: StateFlow<ConfiguracoesUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                val profile = profileService.getUserProfile()
                val goal = profileService.getUserGoal()

                _uiState.value = ConfiguracoesUiState(
                    userProfile = profile,
                    userGoal = goal,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}

