package br.edu.ufam.nutrilogapp.data.model

data class AppConfig(
    val id: Int? = null,
    val tema: String = "dark",
    val notificacoes: Boolean = true,
    val emailAlertas: Boolean = false,
    val metaCaloriasDiarias: Int = 2000,
    val metaCarboidratosDiarios: Int = 250,
    val idioma: String = "pt-BR",
    val formatoData: String = "DD/MM/YYYY",
    val privacidadeDados: Boolean = true,
    val sincronizacaoAutomatica: Boolean = true,
    val alturaCm: Double? = null,
    val pesoKg: Double? = null,
    val dataNascimento: String? = null,
    val sexo: String? = null,
    val nivelAtividade: String = "Sedentário"
)

data class PaginatedResponse<T>(
    val count: Int,
    val next: String? = null,
    val previous: String? = null,
    val results: List<T>
)

