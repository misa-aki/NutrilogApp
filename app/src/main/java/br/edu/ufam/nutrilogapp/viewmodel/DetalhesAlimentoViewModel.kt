package br.edu.ufam.nutrilogapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ufam.nutrilogapp.data.model.Alimento
import br.edu.ufam.nutrilogapp.data.service.AlimentoService
import br.edu.ufam.nutrilogapp.data.service.AlimentoServiceImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DetalhesAlimentoUiState(
    val alimento: Alimento? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class DetalhesAlimentoViewModel(
    private val alimentoService: AlimentoService = AlimentoServiceImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetalhesAlimentoUiState())
    val uiState: StateFlow<DetalhesAlimentoUiState> = _uiState.asStateFlow()

    fun loadAlimento(barcode: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                alimento = null
            )

            alimentoService.getAlimentoByBarcode(barcode)
                .onSuccess { alimento ->
                    _uiState.value = _uiState.value.copy(
                        alimento = alimento,
                        isLoading = false,
                        errorMessage = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Erro ao buscar alimento",
                        alimento = null
                    )
                }
        }
    }
}

