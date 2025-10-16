package com.calyrsoft.ucbp1.features.auth.data.datasource

import com.calyrsoft.ucbp1.features.auth.data.database.dao.IUserDao
import com.calyrsoft.ucbp1.features.auth.data.mapper.toDomain
import com.calyrsoft.ucbp1.features.auth.data.mapper.toEntity

import com.calyrsoft.ucbp1.features.auth.domain.model.User

class AuthLocalDataSource(private val userDao: IUserDao) {

    // DataSource
    suspend fun register(user: User, hash: String): Long {
        val usernameExists = userDao.countByUsername(user.username.toString()) > 0
        val emailExists = userDao.countByEmail(user.email.toString()) > 0

        if (usernameExists) throw Exception("El nombre de usuario ya está en uso")
        if (emailExists) throw Exception("El correo electrónico ya está registrado")

        val entity = com.calyrsoft.ucbp1.features.auth.data.database.entity.UserEntity(
            username = user.username.toString(),
            email = user.email.toString(),
            phone = user.phone?.toString(),
            role = user.role.name,
            // si tu entity guarda el hash, incluye el campo aquí
            passwordHash = hash // <-- ajusta el nombre del campo real de tu Entity
        )
        return userDao.insert(entity)
    }



    suspend fun registerAll(users: List<User>, hashProvider: (User) -> String): List<Long> {
        val entities = users.map { user ->
            com.calyrsoft.ucbp1.features.auth.data.database.entity.UserEntity(

                username = user.username.toString(),
                email = user.email.toString(),
                phone = user.phone?.toString(),
                role = user.role.name,
                passwordHash = hashProvider(user) // <-- ajusta nombre de campo
            )
        }
        return userDao.insertAll(entities)
    }

    suspend fun login(userOrEmail: String, hash: String): User? {
        val e = userDao.login(userOrEmail, hash) ?: return null
        return User.create(

            username = e.username,
            email = e.email,
            phone = e.phone,
            role = e.role
        )
    }

    suspend fun findById(id: Long): User? {
        val e = userDao.findById(id) ?: return null
        return User.create(

            username = e.username,
            email = e.email,
            phone = e.phone,
            role = e.role
        )
    }

    suspend fun existsByUsername(username: String): Boolean {
        return userDao.countByUsername(username) > 0
    }

    suspend fun existsByEmail(email: String): Boolean {
        return userDao.countByEmail(email) > 0
    }
}
