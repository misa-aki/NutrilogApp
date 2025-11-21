package br.edu.ufam.nutrilogapp.data.service

import br.edu.ufam.nutrilogapp.data.model.User

interface UserService {
    suspend fun getCurrentUser(): User
}

class UserServiceImpl : UserService {
    override suspend fun getCurrentUser(): User {
        return User(name = "Samara")
    }
}

