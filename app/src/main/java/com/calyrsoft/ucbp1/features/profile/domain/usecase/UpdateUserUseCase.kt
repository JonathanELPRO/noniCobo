package com.calyrsoft.ucbp1.features.profile.domain.usecase

import com.calyrsoft.ucbp1.features.profile.domain.repository.IUpdateRepository

class UpdateUserUseCase (private val repo: IUpdateRepository) {
    suspend operator fun invoke(id:Long,username:String,phone:String): Result<Unit> {
        return repo.updateUser(id,username,phone)
    }
}