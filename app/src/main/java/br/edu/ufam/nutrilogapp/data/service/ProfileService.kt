package br.edu.ufam.nutrilogapp.data.service

import br.edu.ufam.nutrilogapp.data.api.ApiClient
import br.edu.ufam.nutrilogapp.data.model.FullUserProfile
import br.edu.ufam.nutrilogapp.data.model.ProfileUpdateRequest

interface ProfileService {
    suspend fun getProfile(): Result<FullUserProfile>
    suspend fun updateProfile(request: ProfileUpdateRequest): Result<FullUserProfile>
}

class ProfileServiceImpl : ProfileService {
    private val api = ApiClient.api

    override suspend fun getProfile(): Result<FullUserProfile> {
        return try {
            val response = api.getProfile()

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Erro ao obter perfil"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(request: ProfileUpdateRequest): Result<FullUserProfile> {
        return try {
            val response = api.updateProfile(
                username = request.username,
                email = request.email,
                currentPassword = request.currentPassword,
                newPassword = request.newPassword
            )

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Erro ao atualizar perfil"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
