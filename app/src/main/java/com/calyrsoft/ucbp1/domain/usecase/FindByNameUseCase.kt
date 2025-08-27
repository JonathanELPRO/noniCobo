package com.calyrsoft.ucbp1.domain.usecase

import com.calyrsoft.ucbp1.domain.model.LoginUserModel
import com.calyrsoft.ucbp1.domain.repository.ILoginRepository

class FindByNameUseCase(
    val repository: ILoginRepository,
) {
    fun invoke(name: String): Result<LoginUserModel> {
        return repository.findByName(name)
    }
}