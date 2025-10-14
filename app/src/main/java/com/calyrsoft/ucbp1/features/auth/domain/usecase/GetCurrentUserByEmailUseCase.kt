package com.calyrsoft.ucbp1.features.auth.domain.usecase

import com.calyrsoft.ucbp1.features.auth.domain.model.User
import com.calyrsoft.ucbp1.features.auth.domain.repository.IAuthRepository

class GetCurrentUserByEmailUseCase(private val repo: IAuthRepository) {
    suspend operator fun invoke(email:String): Result<User> {
        return repo.getByEmail(email)
    }
}
