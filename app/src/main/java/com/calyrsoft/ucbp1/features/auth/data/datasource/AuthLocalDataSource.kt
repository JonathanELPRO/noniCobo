package com.calyrsoft.ucbp1.features.auth.data.datasource

import com.calyrsoft.ucbp1.features.auth.data.database.dao.IUserDao
import com.calyrsoft.ucbp1.features.auth.data.mapper.toDomain
import com.calyrsoft.ucbp1.features.auth.data.mapper.toEntity
import com.calyrsoft.ucbp1.features.auth.domain.model.User

class AuthLocalDataSource(private val userDao: IUserDao) {

    // DataSource
    suspend fun register(user: User, hash: String): Long {
        val usernameExists = userDao.countByUsername(user.username) > 0
        val emailExists = userDao.countByEmail(user.email) > 0

        if (usernameExists) throw Exception("El nombre de usuario ya está en uso")
        if (emailExists) throw Exception("El correo electrónico ya está registrado")

        val entity = user.toEntity(hash)
        return userDao.insert(entity)
    }



    suspend fun registerAll(users: List<User>, hashProvider: (User) -> String): List<Long> {
        val entities = users.map { user -> user.toEntity(hashProvider(user)) }
        return userDao.insertAll(entities)
    }

    suspend fun login(userOrEmail: String, hash: String): User? {
        return userDao.login(userOrEmail, hash)?.toDomain()
    }

    suspend fun findById(id: Long): User? {
        return userDao.findById(id)?.toDomain()
    }

    suspend fun existsByUsername(username: String): Boolean {
        return userDao.countByUsername(username) > 0
    }

    suspend fun existsByEmail(email: String): Boolean {
        return userDao.countByEmail(email) > 0
    }
}
