package br.edu.ufam.nutrilogapp.data.model

data class UserProfile(
    val name: String,
    val age: Int,
    val weight: Double,
    val height: Double,
    val profileImageUrl: String? = null
)

data class UserGoal(
    val description: String
)

