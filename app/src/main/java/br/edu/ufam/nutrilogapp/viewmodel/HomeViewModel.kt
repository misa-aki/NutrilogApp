package br.edu.ufam.nutrilogapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ufam.nutrilogapp.data.model.DailyNutrition
import br.edu.ufam.nutrilogapp.data.service.NutritionService
import br.edu.ufam.nutrilogapp.data.service.NutritionServiceImpl
import br.edu.ufam.nutrilogapp.data.service.UserService
import br.edu.ufam.nutrilogapp.data.service.UserServiceImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val userName: String = "",
    val dailyNutrition: DailyNutrition = DailyNutrition(
        nutrients = br.edu.ufam.nutrilogapp.data.model.Nutrients(
            total = 0.0,
            sugars = 0.0,
            carbohydrates = 0.0,
            proteins = 0.0,
            fats = 0.0,
            fibers = 0.0
        ),
        averageGI = emptyList()
    ),
    val isLoading: Boolean = true
)

class HomeViewModel(
    private val nutritionService: NutritionService = NutritionServiceImpl(),
    private val userService: UserService = UserServiceImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                val user = userService.getCurrentUser()
                val nutrition = nutritionService.getDailyNutrition()

                _uiState.value = HomeUiState(
                    userName = user.name,
                    dailyNutrition = nutrition,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}

