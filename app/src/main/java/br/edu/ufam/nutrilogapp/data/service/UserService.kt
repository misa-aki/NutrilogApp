package br.edu.ufam.nutrilogapp.data.service

import br.edu.ufam.nutrilogapp.data.api.ApiClient
import br.edu.ufam.nutrilogapp.data.model.User

interface UserService {
    suspend fun getCurrentUser(): Result<User>
}

class UserServiceImpl : UserService {
    private val api = ApiClient.api

    override suspend fun getCurrentUser(): Result<User> {
        return try {
            val response = api.getSession()

            if (response.isSuccessful && response.body() != null) {
                val sessionResponse = response.body()!!
                
                if (sessionResponse.authenticated && sessionResponse.user != null) {
                    val userInfo = sessionResponse.user!!
                    val user = User(
                        id = userInfo.id,
                        username = userInfo.username,
                        email = userInfo.email
                    )
                    Result.success(user)
                } else {
                    Result.failure(Exception("Usuário não autenticado"))
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Erro ao obter informações do usuário"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

