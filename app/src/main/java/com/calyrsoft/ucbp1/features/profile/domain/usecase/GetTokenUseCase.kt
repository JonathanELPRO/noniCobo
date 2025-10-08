package com.calyrsoft.ucbp1.features.profile.domain.usecase

import com.calyrsoft.ucbp1.features.profile.domain.repository.ILoginRepository

class GetTokenUseCase(
    private val repository: ILoginRepository
) {
    suspend operator fun invoke(): Result<String> {
        return repository.getToken()
    }
}
