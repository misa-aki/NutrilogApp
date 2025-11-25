package br.edu.ufam.nutrilogapp.data.model

data class SessionResponse(
    val authenticated: Boolean,
    val user: UserInfo?
)

data class UserInfo(
    val id: Int,
    val username: String,
    val email: String
)

