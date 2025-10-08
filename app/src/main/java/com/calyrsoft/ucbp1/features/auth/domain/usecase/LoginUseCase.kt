package com.calyrsoft.ucbp1.features.auth.domain.usecase

import com.calyrsoft.ucbp1.features.auth.domain.model.User
import com.calyrsoft.ucbp1.features.auth.domain.repository.IAuthRepository

class LoginUseCase(private val repo: IAuthRepository) {
    suspend operator fun invoke(userOrEmail: String, passwordPlain: String): User? = repo.login(userOrEmail, passwordPlain)
}