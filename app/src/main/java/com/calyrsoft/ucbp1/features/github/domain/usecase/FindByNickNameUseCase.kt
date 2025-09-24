package com.calyrsoft.ucbp1.features.github.domain.usecase

import com.calyrsoft.ucbp1.features.github.domain.model.UserModel
import com.calyrsoft.ucbp1.features.github.domain.repository.IGithubRepository

class FindByNickNameUseCase(
    val repository: IGithubRepository,
) {
    suspend fun invoke(nickname: String): Result<UserModel> {
        return repository.findByNick(nickname)
    }
}