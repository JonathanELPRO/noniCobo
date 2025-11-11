package com.calyrsoft.ucbp1.features.profile.domain.usecase

import com.calyrsoft.ucbp1.features.profile.domain.repository.IUpdateRepository

class UpdateUserPasswordUseCase (private val repo: IUpdateRepository) {
    suspend operator fun invoke(password:String,token:String): Result<Unit> {
        return repo.updateUserPassword(password,token)
    }
}