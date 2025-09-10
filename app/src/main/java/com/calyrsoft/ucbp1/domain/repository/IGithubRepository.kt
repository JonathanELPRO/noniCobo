package com.calyrsoft.ucbp1.domain.repository

import com.calyrsoft.ucbp1.domain.model.UserModel

interface IGithubRepository {
    suspend fun findByNick(value: String): Result<UserModel>
}
