package br.edu.ufam.nutrilogapp.data.service

import br.edu.ufam.nutrilogapp.data.model.UserGoal
import br.edu.ufam.nutrilogapp.data.model.UserProfile

interface ProfileService {
    suspend fun getUserProfile(): UserProfile
    suspend fun getUserGoal(): UserGoal
}

class ProfileServiceImpl : ProfileService {
    override suspend fun getUserProfile(): UserProfile {
        return UserProfile(
            name = "Samara",
            age = 22,
            weight = 54.7,
            height = 1.62
        )
    }

    override suspend fun getUserGoal(): UserGoal {
        return UserGoal(description = "Ganhar 5kg")
    }
}

