package com.calyrsoft.ucbp1.features.auth.domain.repository

import com.calyrsoft.ucbp1.features.auth.domain.model.User


interface IAuthRepository {
    suspend fun register(user: User, passwordPlain: String): Long
    suspend fun login(userOrEmail: String, passwordPlain: String): User?
    suspend fun getById(id: Long): User?
}