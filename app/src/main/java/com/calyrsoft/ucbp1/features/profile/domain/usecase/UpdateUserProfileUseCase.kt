package com.calyrsoft.ucbp1.features.profile.domain.usecase

import com.calyrsoft.ucbp1.features.profile.domain.model.LoginUserModel
import com.calyrsoft.ucbp1.features.profile.domain.repository.ILoginRepository

class UpdateUserProfileUseCase(
    private val repository: ILoginRepository,
) {
    fun invoke(
        name: String,
        newName: String? = null,
        newPhone: String? = null,
        newImageUrl: String? = null,
        newPassword: String? = null
    ): Result<LoginUserModel> {
        return repository.updateUserProfile(name, newName, newPhone, newImageUrl, newPassword)
    }
}



