package br.edu.ufam.nutrilogapp.data.service

import android.util.Log
import br.edu.ufam.nutrilogapp.data.api.ApiClient
import br.edu.ufam.nutrilogapp.data.model.AppConfig

interface ConfigService {
    suspend fun getConfiguracoes(): Result<AppConfig?>
    suspend fun saveConfiguracoes(config: AppConfig): Result<AppConfig>
}

class ConfigServiceImpl : ConfigService {
    private val api = ApiClient.api

    override suspend fun getConfiguracoes(): Result<AppConfig?> {
        return try {
            val response = api.getConfiguracoes()

            if (response.isSuccessful && response.body() != null) {
                val paginatedResponse = response.body()!!
                val config = paginatedResponse.results.firstOrNull()
                Result.success(config)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Erro ao obter configurações"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveConfiguracoes(config: AppConfig): Result<AppConfig> {
        return try {
            Log.d("saveConfiguracoes", config.id.toString())

            val response = if (config.id != null && config.id > 0) {
                val id = config.id

                api.updateConfiguracoes(id, config)
            } else {
                api.createConfiguracoes(config)
            }

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Erro ao salvar configurações"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

