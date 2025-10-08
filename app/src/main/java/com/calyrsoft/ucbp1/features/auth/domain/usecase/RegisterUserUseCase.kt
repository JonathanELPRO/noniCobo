package com.calyrsoft.ucbp1.features.auth.domain.usecase

import com.calyrsoft.ucbp1.features.auth.domain.model.User
import com.calyrsoft.ucbp1.features.auth.domain.repository.IAuthRepository

class RegisterUserUseCase(private val repo: IAuthRepository) {
    suspend operator fun invoke(user: User, passwordPlain: String): Long = repo.register(user, passwordPlain)
}