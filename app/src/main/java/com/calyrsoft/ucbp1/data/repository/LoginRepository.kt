package com.calyrsoft.ucbp1.data.repository

import com.calyrsoft.ucbp1.domain.model.LoginUserModel
import com.calyrsoft.ucbp1.domain.repository.ILoginRepository


class LoginRepository : ILoginRepository {
    private val users = listOf(
        LoginUserModel("calyr", "1234", "12345678", "https://avatars.githubusercontent.com/u/874321?v=4"),
        LoginUserModel("admin", "abcd","87654321", "https://avatars.githubusercontent.com/u/874321?v=4")
    )

    override fun findByNameAndPassword(name: String, password: String): Result<LoginUserModel> {
        val user = users.find { it.name == name && it.password == password }

        return if (user != null) {
            Result.success(user)
        } else {
            Result.failure(Exception("Usuario o contraseña incorrectos"))
        }
    }

    override fun findByName(name: String): Result<LoginUserModel> {
        val user = users.find {it.name == name}

        return if (user != null) {
            Result.success(user)
        } else {
            Result.failure(Exception("Usuario o contraseña incorrectos"))
        }
    }

    override fun updateUserProfile(
        name: String,
        newName: String?,
        newPhone: String?,
        newImageUrl: String?
    ): Result<LoginUserModel> {
        val user = users.find { it.name == name } ?: return Result.failure(Exception("Usuario no encontrado"))

        val updatedUser = user.copy(
            name = newName ?: user.name,
            phone = newPhone ?: user.phone,
            imageUrl = newImageUrl ?: user.imageUrl
        )

        return Result.success(updatedUser)
    }
}