package br.edu.ufam.nutrilogapp.data.model

data class Alimento(
    val id: Int,
    val nome: String,
    val openfoodfacts_id: String,
    val marca: String? = null,
    val categoria: String? = null,
    val carboidratos: Double,
    val acucares: Double,
    val fibras: Double,
    val calorias: Double,
    val proteinas: Double,
    val gorduras: Double,
    val porcao_padrao: Double? = null,
    val imagem_url: String? = null,
    val classificacao_carboidratos: String? = null,
    val porcoes_carboidrato: Double? = null,
    val mensagem_diabetes: String? = null
)

