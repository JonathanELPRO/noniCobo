package com.calyrsoft.ucbp1.features.auth.data.repository

import com.calyrsoft.ucbp1.features.auth.data.database.entity.UserEntity
import com.calyrsoft.ucbp1.features.auth.data.datasource.AuthLocalDataSource
import com.calyrsoft.ucbp1.features.auth.domain.model.Role
import com.calyrsoft.ucbp1.features.auth.domain.model.User
import com.calyrsoft.ucbp1.features.auth.domain.repository.IAuthRepository


class AuthRepository(private val ds: AuthLocalDataSource) : IAuthRepository {
    private fun hash(p: String) = p.reversed()


    override suspend fun register(user: User, passwordPlain: String): Long {
        val entity = UserEntity(
            username = user.username,
            email = user.email,
            phone = user.phone,
            passwordHash = hash(passwordPlain),
            role = user.role.name
        )
        return ds.register(entity)
    }


    override suspend fun login(userOrEmail: String, passwordPlain: String): User? =
        ds.login(userOrEmail, hash(passwordPlain))?.let {
            User(
                id = it.id,
                username = it.username,
                email = it.email,
                phone = it.phone,
                role = Role.valueOf(it.role)
            )
        }


    override suspend fun getById(id: Long): User? = ds.findById(id)?.let {
        User(it.id, it.username, it.email, it.phone, Role.valueOf(it.role))
    }
}