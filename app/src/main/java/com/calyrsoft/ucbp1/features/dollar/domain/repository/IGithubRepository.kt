package com.calyrsoft.ucbp1.features.dollar.domain.repository

import com.calyrsoft.ucbp1.features.dollar.domain.model.UserModel

interface IGithubRepository {
    suspend fun findByNick(value: String): Result<UserModel>
}
