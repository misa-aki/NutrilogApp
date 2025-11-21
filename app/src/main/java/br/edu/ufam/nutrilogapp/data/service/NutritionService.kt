package br.edu.ufam.nutrilogapp.data.service

import br.edu.ufam.nutrilogapp.data.model.DailyNutrition
import br.edu.ufam.nutrilogapp.data.model.GIDataPoint
import br.edu.ufam.nutrilogapp.data.model.Nutrients

interface NutritionService {
    suspend fun getDailyNutrition(): DailyNutrition
}

class NutritionServiceImpl : NutritionService {
    override suspend fun getDailyNutrition(): DailyNutrition {
        return DailyNutrition(
            nutrients = Nutrients(
                total = 357.7,
                sugars = 19.9,
                carbohydrates = 64.6,
                proteins = 3.1,
                fats = 8.9,
                fibers = 19.0
            ),
            averageGI = listOf(
                GIDataPoint("08:00", 45f),
                GIDataPoint("10:00", 62f),
                GIDataPoint("12:00", 58f),
                GIDataPoint("14:00", 70f),
                GIDataPoint("16:00", 55f),
                GIDataPoint("18:00", 48f)
            )
        )
    }
}

