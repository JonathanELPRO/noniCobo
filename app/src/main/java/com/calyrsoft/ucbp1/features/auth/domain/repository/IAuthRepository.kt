package com.calyrsoft.ucbp1.features.auth.domain.repository

import com.calyrsoft.ucbp1.features.auth.domain.model.Role
import com.calyrsoft.ucbp1.features.auth.domain.model.User

interface IAuthRepository {
    suspend fun register(user: User, passwordPlain: String): Result<Long>
    suspend fun registerToSupabase(user:User,passwordPlain: String): Result<User>
    suspend fun login(userOrEmail: String, passwordPlain: String): Result<User>
    suspend fun getById(id: Long): Result<User>
    suspend fun getByEmail(email: String): Result<User>

    suspend fun  loginWithSupabase(email:String,password:String): Result<User>
}
