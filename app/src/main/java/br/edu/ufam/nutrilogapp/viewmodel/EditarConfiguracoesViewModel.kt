package br.edu.ufam.nutrilogapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ufam.nutrilogapp.data.model.AppConfig
import br.edu.ufam.nutrilogapp.data.model.FullUserProfile
import br.edu.ufam.nutrilogapp.data.model.ProfileUpdateRequest
import br.edu.ufam.nutrilogapp.data.service.ConfigService
import br.edu.ufam.nutrilogapp.data.service.ConfigServiceImpl
import br.edu.ufam.nutrilogapp.data.service.ProfileService
import br.edu.ufam.nutrilogapp.data.service.ProfileServiceImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EditarConfiguracoesUiState(
    val profile: FullUserProfile? = null,
    val config: AppConfig? = null,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class EditarConfiguracoesViewModel(
    private val profileService: ProfileService = ProfileServiceImpl(),
    private val configService: ConfigService = ConfigServiceImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditarConfiguracoesUiState())
    val uiState: StateFlow<EditarConfiguracoesUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            val profileResult = profileService.getProfile()
            val configResult = configService.getConfiguracoes()
            
            profileResult.onSuccess { profile ->
                configResult.onSuccess { config ->
                    _uiState.value = _uiState.value.copy(
                        profile = profile,
                        config = config,
                        isLoading = false
                    )
                }.onFailure {
                    _uiState.value = _uiState.value.copy(
                        profile = profile,
                        isLoading = false
                    )
                }
            }.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = exception.message ?: "Erro ao carregar dados"
                )
            }
        }
    }

    fun updateProfile(
        profileRequest: ProfileUpdateRequest,
        configData: AppConfig,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSaving = true,
                errorMessage = null,
                successMessage = null
            )

            val baseConfig = _uiState.value.config
            val configToSave = if (baseConfig != null && baseConfig.id != null && baseConfig.id!! > 0) {
                baseConfig.copy(
                    tema = baseConfig.tema,
                    notificacoes = baseConfig.notificacoes,
                    emailAlertas = baseConfig.emailAlertas,
                    metaCaloriasDiarias = baseConfig.metaCaloriasDiarias,
                    metaCarboidratosDiarios = baseConfig.metaCarboidratosDiarios,
                    idioma = baseConfig.idioma,
                    formatoData = baseConfig.formatoData,
                    privacidadeDados = baseConfig.privacidadeDados,
                    sincronizacaoAutomatica = baseConfig.sincronizacaoAutomatica,
                    alturaCm = configData.alturaCm,
                    pesoKg = configData.pesoKg,
                    dataNascimento = configData.dataNascimento,
                    sexo = configData.sexo,
                    nivelAtividade = configData.nivelAtividade,
                    id = baseConfig.id
                )
            } else {
                AppConfig(
                    tema = baseConfig?.tema ?: "dark",
                    notificacoes = baseConfig?.notificacoes ?: true,
                    emailAlertas = baseConfig?.emailAlertas ?: false,
                    metaCaloriasDiarias = baseConfig?.metaCaloriasDiarias ?: 2000,
                    metaCarboidratosDiarios = baseConfig?.metaCarboidratosDiarios ?: 250,
                    idioma = baseConfig?.idioma ?: "pt-BR",
                    formatoData = baseConfig?.formatoData ?: "DD/MM/YYYY",
                    privacidadeDados = baseConfig?.privacidadeDados ?: true,
                    sincronizacaoAutomatica = baseConfig?.sincronizacaoAutomatica ?: true,
                    alturaCm = configData.alturaCm,
                    pesoKg = configData.pesoKg,
                    dataNascimento = configData.dataNascimento,
                    sexo = configData.sexo,
                    nivelAtividade = configData.nivelAtividade,
                    id = null
                )
            }

            configService.saveConfiguracoes(configToSave)
                .onSuccess { savedConfig ->
                    _uiState.value = _uiState.value.copy(
                        config = savedConfig,
                        isSaving = false,
                        successMessage = "Perfil atualizado com sucesso!"
                    )
                    onSuccess()
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        errorMessage = exception.message ?: "Erro ao salvar configurações"
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun clearSuccess() {
        _uiState.value = _uiState.value.copy(successMessage = null)
    }
}

