package br.edu.ufam.nutrilogapp.data.api

import android.content.Context
import br.edu.ufam.nutrilogapp.data.auth.AuthManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL = "http://162.248.101.238:8000/"

    private var retrofit: Retrofit? = null

    fun initialize(context: Context) {
        val authManager = AuthManager(context)
        val authInterceptor = AuthInterceptor(authManager)
        
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: NutrilogApi
        get() {
            if (retrofit == null) {
                throw IllegalStateException("ApiClient não foi inicializado. Chame ApiClient.initialize(context) primeiro.")
            }
            return retrofit!!.create(NutrilogApi::class.java)
        }
}

