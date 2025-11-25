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

data class ScannerUiState(
    val scannedBarcode: String = "",
    val alimento: Alimento? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class ScannerViewModel(
    private val alimentoService: AlimentoService = AlimentoServiceImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScannerUiState())
    val uiState: StateFlow<ScannerUiState> = _uiState.asStateFlow()

    fun onBarcodeDetected(barcode: String) {
        _uiState.value = _uiState.value.copy(scannedBarcode = barcode)
    }

    fun loadAlimento(barcode: String, onSuccess: (Alimento) -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            alimentoService.getAlimentoByBarcode(barcode)
                .onSuccess { alimento ->
                    _uiState.value = _uiState.value.copy(
                        alimento = alimento,
                        isLoading = false
                    )
                    onSuccess(alimento)
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Erro ao buscar alimento"
                    )
                }
        }
    }

    fun resetScanner() {
        _uiState.value = _uiState.value.copy(
            scannedBarcode = "",
            alimento = null,
            errorMessage = null
        )
    }
}

