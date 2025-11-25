package br.edu.ufam.nutrilogapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ufam.nutrilogapp.data.model.UserGoal
import br.edu.ufam.nutrilogapp.data.model.UserProfile
import br.edu.ufam.nutrilogapp.data.service.ConfigService
import br.edu.ufam.nutrilogapp.data.service.ConfigServiceImpl
import br.edu.ufam.nutrilogapp.data.service.ProfileService
import br.edu.ufam.nutrilogapp.data.service.ProfileServiceImpl
import br.edu.ufam.nutrilogapp.data.service.UserService
import br.edu.ufam.nutrilogapp.data.service.UserServiceImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

data class ConfiguracoesUiState(
    val userProfile: UserProfile? = null,
    val userGoal: UserGoal? = null,
    val sexo: String? = null,
    val nivelAtividade: String? = null,
    val isLoading: Boolean = true
)

class ConfiguracoesViewModel(
    private val profileService: ProfileService = ProfileServiceImpl(),
    private val configService: ConfigService = ConfigServiceImpl(),
    private val userService: UserService = UserServiceImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConfiguracoesUiState())
    val uiState: StateFlow<ConfiguracoesUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            val userResult = userService.getCurrentUser()
            val configResult = configService.getConfiguracoes()
            
            userResult.onSuccess { user ->
                configResult.onSuccess { config ->
                    val age = config?.dataNascimento?.let { calculateAge(it) } ?: 0
                    val userProfile = UserProfile(
                        name = user.name,
                        age = age,
                        weight = config?.pesoKg ?: 0.0,
                        height = (config?.alturaCm ?: 0.0) / 100.0
                    )
                    
                    val userGoal = UserGoal(description = "Ganhar 5kg")
                    
                    _uiState.value = ConfiguracoesUiState(
                        userProfile = userProfile,
                        userGoal = userGoal,
                        sexo = config?.sexo,
                        nivelAtividade = config?.nivelAtividade,
                        isLoading = false
                    )
                }.onFailure {
                    val userProfile = UserProfile(
                        name = user.name,
                        age = 0,
                        weight = 0.0,
                        height = 0.0
                    )
                    _uiState.value = ConfiguracoesUiState(
                        userProfile = userProfile,
                        userGoal = UserGoal(description = "Ganhar 5kg"),
                        sexo = null,
                        nivelAtividade = null,
                        isLoading = false
                    )
                }
            }.onFailure {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    private fun calculateAge(dataNascimento: String): Int {
        return try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val birthDate = LocalDate.parse(dataNascimento, formatter)
            ChronoUnit.YEARS.between(birthDate, LocalDate.now()).toInt()
        } catch (e: Exception) {
            0
        }
    }
}

