package com.calyrsoft.ucbp1.data.repository

import com.calyrsoft.ucbp1.domain.model.LoginUserModel
import com.calyrsoft.ucbp1.domain.repository.ILoginRepository


class LoginRepository : ILoginRepository {
    private val users = listOf(
        LoginUserModel("calyr", "1234"),
        LoginUserModel("admin", "abcd")
    )

    override fun findByNameAndPassword(name: String, password: String): Result<LoginUserModel> {
        val user = users.find { it.name == name && it.password == password }

        return if (user != null) {
            Result.success(user)
        } else {
            Result.failure(Exception("Usuario o contraseña incorrectos"))
        }
    }
}