package com.calyrsoft.ucbp1.features.profile.domain.usecase

import com.calyrsoft.ucbp1.features.profile.domain.repository.ILoginRepository

class SaveTokenUseCase(
    private val repository: ILoginRepository
) {
    suspend operator fun invoke(token: String): Result<Unit> {
        return repository.saveToken(token)
    }
}