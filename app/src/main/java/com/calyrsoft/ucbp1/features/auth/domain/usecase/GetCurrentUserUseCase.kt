package com.calyrsoft.ucbp1.features.auth.domain.usecase

import com.calyrsoft.ucbp1.features.auth.domain.model.User
import com.calyrsoft.ucbp1.features.auth.domain.repository.IAuthRepository

class GetCurrentUserUseCase(private val repo: IAuthRepository) {
    suspend operator fun invoke(id: Long): User? = repo.getById(id)
}