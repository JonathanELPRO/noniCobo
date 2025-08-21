package com.calyrsoft.ucbp1.domain.usecase

import com.calyrsoft.ucbp1.domain.model.UserModel
import com.calyrsoft.ucbp1.domain.repository.IGithubRepository

class FindByNickNameUseCase(
    val repository: IGithubRepository,
) {
    fun invoke(nickname: String): Result<UserModel> {
        return repository.findByNick(nickname)
    }
}
