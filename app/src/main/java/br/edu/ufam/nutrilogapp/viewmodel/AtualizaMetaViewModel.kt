package br.edu.ufam.nutrilogapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ufam.nutrilogapp.data.model.GoalOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AtualizaMetaUiState(
    val goalOptions: List<GoalOption> = listOf(
        GoalOption(id = "lose", description = "Quero perder 2kg."),
        GoalOption(id = "maintain", description = "Quero manter o peso."),
        GoalOption(id = "gain", description = "Quero ganhar 10kg.")
    ),
    val selectedGoalId: String = "maintain"
)

class AtualizaMetaViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AtualizaMetaUiState())
    val uiState: StateFlow<AtualizaMetaUiState> = _uiState.asStateFlow()

    fun selectGoal(goalId: String) {
        _uiState.value = _uiState.value.copy(selectedGoalId = goalId)
    }

    fun getSelectedGoal(): GoalOption? {
        return _uiState.value.goalOptions.find { it.id == _uiState.value.selectedGoalId }
    }
}

