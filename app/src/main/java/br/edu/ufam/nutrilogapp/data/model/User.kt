package br.edu.ufam.nutrilogapp.data.model

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val profileImageUrl: String? = null
) {
    val name: String
        get() = username
}

