package com.calyrsoft.ucbp1.domain.repository

import com.calyrsoft.ucbp1.domain.model.LoginUserModel

interface ILoginRepository {
    fun findByNameAndPassword(name: String, password: String): Result<LoginUserModel>
}