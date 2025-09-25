package com.calyrsoft.ucbp1.features.profile.domain.usecase

import com.calyrsoft.ucbp1.features.profile.domain.model.LoginUserModel
import com.calyrsoft.ucbp1.features.profile.domain.repository.ILoginRepository

class FindByNameUseCase(
    val repository: ILoginRepository,
) {
    operator fun invoke(name: String): Result<LoginUserModel> {
        return repository.findByName(name)
    }
}