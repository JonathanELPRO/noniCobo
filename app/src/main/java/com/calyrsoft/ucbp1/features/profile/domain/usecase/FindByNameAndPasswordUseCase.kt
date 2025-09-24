package com.calyrsoft.ucbp1.features.profile.domain.usecase

import com.calyrsoft.ucbp1.features.profile.domain.model.LoginUserModel
import com.calyrsoft.ucbp1.features.profile.domain.repository.ILoginRepository

class FindByNameAndPasswordUseCase(
    val repository: ILoginRepository,
) {
    fun invoke(name: String, password: String): Result<LoginUserModel> {
        return repository.findByNameAndPassword(name, password)
    }
}
