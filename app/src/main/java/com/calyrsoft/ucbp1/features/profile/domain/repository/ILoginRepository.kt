package com.calyrsoft.ucbp1.features.profile.domain.repository

import com.calyrsoft.ucbp1.features.profile.domain.model.LoginUserModel

interface ILoginRepository {
    fun findByNameAndPassword(name: String, password: String): Result<LoginUserModel>
    fun findByName(name: String): Result<LoginUserModel>

    fun updateUserProfile(
        name: String,
        newName: String? = null,
        newPhone: String? = null,
        newImageUrl: String? = null,
        newPassword: String? = null,
        newEmail: String? = null,
        newSummary: String? = null
    ): Result<LoginUserModel>

}