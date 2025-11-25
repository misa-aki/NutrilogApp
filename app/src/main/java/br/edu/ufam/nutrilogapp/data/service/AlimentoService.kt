package br.edu.ufam.nutrilogapp.data.service

import br.edu.ufam.nutrilogapp.data.api.ApiClient
import br.edu.ufam.nutrilogapp.data.model.Alimento

interface AlimentoService {
    suspend fun getAlimentoByBarcode(barcode: String): Result<Alimento>
}

class AlimentoServiceImpl : AlimentoService {
    private val api = ApiClient.api

    override suspend fun getAlimentoByBarcode(barcode: String): Result<Alimento> {
        return try {
            val response = api.getAlimentoByBarcode(barcode)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Erro ao buscar alimento"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

