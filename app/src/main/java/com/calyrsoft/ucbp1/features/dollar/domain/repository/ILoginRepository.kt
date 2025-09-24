package com.calyrsoft.ucbp1.features.dollar.domain.repository

import com.calyrsoft.ucbp1.features.dollar.domain.model.LoginUserModel

interface ILoginRepository {
    fun findByNameAndPassword(name: String, password: String): Result<LoginUserModel>
    fun findByName(name: String): Result<LoginUserModel>

    fun updateUserProfile(
        name: String,
        newName: String? = null,
        newPhone: String? = null,
        newImageUrl: String? = null,
        newPassword: String? = null
    ): Result<LoginUserModel>

}