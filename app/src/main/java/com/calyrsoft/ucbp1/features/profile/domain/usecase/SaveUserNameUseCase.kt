package com.calyrsoft.ucbp1.features.profile.domain.usecase

import com.calyrsoft.ucbp1.features.profile.domain.repository.ILoginRepository

class SaveUserNameUseCase(
    private val repository: ILoginRepository
) {
    suspend operator fun invoke(userName: String): Result<Unit> {
        return repository.saveUserName(userName)
    }
}
