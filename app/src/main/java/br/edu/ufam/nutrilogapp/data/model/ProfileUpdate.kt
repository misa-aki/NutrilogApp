package br.edu.ufam.nutrilogapp.data.model

data class ProfileUpdateRequest(
    val username: String? = null,
    val email: String? = null,
    val currentPassword: String? = null,
    val newPassword: String? = null
)

data class FullUserProfile(
    val id: Int,
    val username: String,
    val email: String
)

