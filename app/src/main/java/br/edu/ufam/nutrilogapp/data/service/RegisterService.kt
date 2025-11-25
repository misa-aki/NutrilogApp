package br.edu.ufam.nutrilogapp.data.service

import br.edu.ufam.nutrilogapp.data.api.ApiClient
import br.edu.ufam.nutrilogapp.data.auth.AuthManager
import br.edu.ufam.nutrilogapp.data.model.LoginResponse
import br.edu.ufam.nutrilogapp.data.model.RegisterRequest

interface RegisterService {
    suspend fun register(request: RegisterRequest): Result<LoginResponse>
}

class RegisterServiceImpl(
    private val authManager: AuthManager
) : RegisterService {
    private val api = ApiClient.api

    override suspend fun register(request: RegisterRequest): Result<LoginResponse> {
        return try {
            val response = api.register(
                username = request.username,
                email = request.email,
                password = request.password
            )

            if (response.isSuccessful && response.body() != null) {
                val registerResponse = response.body()!!
                
                if (registerResponse.id != null && registerResponse.username != null) {
                    val token = response.headers()["Authorization"] 
                        ?: response.headers()["X-Auth-Token"]
                        ?: registerResponse.id.toString()
                    
                    authManager.saveToken(token)
                    
                    val setCookieHeaders = response.headers().values("Set-Cookie")
                    var csrfToken: String? = null
                    var sessionId: String? = null
                    
                    for (cookieHeader in setCookieHeaders) {
                        when {
                            cookieHeader.startsWith("csrftoken=") -> {
                                csrfToken = cookieHeader.substringAfter("csrftoken=")
                                    .substringBefore(";")
                            }
                            cookieHeader.startsWith("sessionid=") -> {
                                sessionId = cookieHeader.substringAfter("sessionid=")
                                    .substringBefore(";")
                            }
                        }
                    }
                    
                    if (csrfToken != null && sessionId != null) {
                        authManager.saveCookies(csrfToken, sessionId)
                    }
                    
                    Result.success(registerResponse)
                } else if (registerResponse.detail != null) {
                    Result.failure(Exception(registerResponse.detail))
                } else {
                    Result.failure(Exception("Resposta inválida do servidor"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = if (errorBody != null) {
                    try {
                        com.google.gson.Gson().fromJson(errorBody, LoginResponse::class.java)
                    } catch (e: Exception) {
                        null
                    }
                } else null
                
                val errorMessage = errorResponse?.detail ?: "Erro ao criar conta"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

