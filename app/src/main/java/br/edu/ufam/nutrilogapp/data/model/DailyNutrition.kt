package br.edu.ufam.nutrilogapp.data.model

data class DailyNutrition(
    val nutrients: Nutrients,
    val averageGI: List<GIDataPoint>
)

data class Nutrients(
    val total: Double,
    val sugars: Double,
    val carbohydrates: Double,
    val proteins: Double,
    val fats: Double,
    val fibers: Double
)

data class GIDataPoint(
    val time: String,
    val value: Float
)

