package com.calyrsoft.ucbp1.features.dollar.domain.usecase

import com.calyrsoft.ucbp1.features.dollar.domain.model.LoginUserModel
import com.calyrsoft.ucbp1.features.dollar.domain.repository.ILoginRepository

class FindByNameUseCase(
    val repository: ILoginRepository,
) {
    fun invoke(name: String): Result<LoginUserModel> {
        return repository.findByName(name)
    }
}