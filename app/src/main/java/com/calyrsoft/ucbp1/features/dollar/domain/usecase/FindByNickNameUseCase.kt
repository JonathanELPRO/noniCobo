package com.calyrsoft.ucbp1.features.dollar.domain.usecase

import com.calyrsoft.ucbp1.features.dollar.domain.model.UserModel
import com.calyrsoft.ucbp1.features.dollar.domain.repository.IGithubRepository

class FindByNickNameUseCase(
    val repository: IGithubRepository,
) {
    suspend fun invoke(nickname: String): Result<UserModel> {
        return repository.findByNick(nickname)
    }
}
