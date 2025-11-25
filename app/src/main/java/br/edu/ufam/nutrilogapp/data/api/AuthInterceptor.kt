package br.edu.ufam.nutrilogapp.data.api

import br.edu.ufam.nutrilogapp.data.auth.AuthManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val authManager: AuthManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { authManager.getToken() }
        val csrfToken = runBlocking { authManager.getCsrfToken() }
        val sessionId = runBlocking { authManager.getSessionId() }
        
        val requestBuilder = chain.request().newBuilder()
        
        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }
        
        if (!csrfToken.isNullOrEmpty()) {
            requestBuilder.addHeader("X-CSRFToken", csrfToken)
            
            val cookies = buildString {
                append("csrftoken=$csrfToken")
                if (!sessionId.isNullOrEmpty()) {
                    append("; sessionid=$sessionId")
                }
            }
            requestBuilder.addHeader("Cookie", cookies)
        } else if (!sessionId.isNullOrEmpty()) {
            requestBuilder.addHeader("Cookie", "sessionid=$sessionId")
        }
        
        return chain.proceed(requestBuilder.build())
    }
}

