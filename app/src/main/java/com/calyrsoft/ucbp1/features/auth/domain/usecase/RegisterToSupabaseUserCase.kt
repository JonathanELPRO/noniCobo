package com.calyrsoft.ucbp1.features.auth.domain.usecase

import com.calyrsoft.ucbp1.features.auth.domain.model.User
import com.calyrsoft.ucbp1.features.auth.domain.repository.IAuthRepository

class RegisterToSupabaseUserCase (private val repo: IAuthRepository) {
    suspend operator fun invoke(user: User, passwordPlain: String): Result<User> {
        return repo.registerToSupabase(user,passwordPlain)
    }
}