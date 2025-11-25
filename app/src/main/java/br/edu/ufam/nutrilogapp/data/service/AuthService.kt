package br.edu.ufam.nutrilogapp.data.service

import br.edu.ufam.nutrilogapp.data.api.ApiClient
import br.edu.ufam.nutrilogapp.data.auth.AuthManager
import br.edu.ufam.nutrilogapp.data.model.LoginResponse

interface AuthService {
    suspend fun login(username: String, password: String): Result<LoginResponse>
    suspend fun logout()
    suspend fun isAuthenticated(): Boolean
}

class AuthServiceImpl(
    private val authManager: AuthManager
) : AuthService {
    private val api = ApiClient.api

    override suspend fun login(username: String, password: String): Result<LoginResponse> {
        return try {
            val response = api.login(username, password)

            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!
                
                if (loginResponse.id != null && loginResponse.username != null) {
                    val token = response.headers()["Authorization"] 
                        ?: response.headers()["X-Auth-Token"]
                        ?: loginResponse.id.toString()
                    
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
                    
                    Result.success(loginResponse)
                } else if (loginResponse.detail != null) {
                    Result.failure(Exception(loginResponse.detail))
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
                
                val errorMessage = errorResponse?.detail ?: "Erro ao fazer login"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        authManager.clearToken()
    }

    override suspend fun isAuthenticated(): Boolean {
        val token = authManager.getToken()
        return !token.isNullOrEmpty()
    }
}

