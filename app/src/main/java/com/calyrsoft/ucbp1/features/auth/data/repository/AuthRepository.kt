package com.calyrsoft.ucbp1.features.auth.data.repository

import com.calyrsoft.ucbp1.features.auth.data.datasource.AuthLocalDataSource
import com.calyrsoft.ucbp1.features.auth.domain.model.User
import com.calyrsoft.ucbp1.features.auth.domain.repository.IAuthRepository
import java.security.MessageDigest

class AuthRepository(
    private val ds: AuthLocalDataSource
) : IAuthRepository {

    private fun hash(password: String): String {
        val bytes = MessageDigest
            .getInstance("SHA-256")
            .digest(password.toByteArray(Charsets.UTF_8))

        // convertir bytes → string hexadecimal
        return bytes.joinToString("") { "%02x".format(it) }
    }

    override suspend fun register(user: User, passwordPlain: String): Result<Long> {
        return try {

            if (ds.existsByEmail(user.email) and ds.existsByUsername(user.username))
                return Result.failure(Exception("El correo electrónico y el nombre de usuario ya estan registrados"))

            if (ds.existsByUsername(user.username))
                return Result.failure(Exception("El nombre de usuario ya está en uso"))

            if (ds.existsByEmail(user.email))
                return Result.failure(Exception("El correo electrónico ya está registrado"))


            val id = ds.register(user, hash(passwordPlain))
            Result.success(id)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    override suspend fun login(userOrEmail: String, passwordPlain: String): Result<User> {
        return try {
            val hashed = hash(passwordPlain)
            val user = ds.login(userOrEmail, hashed)
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("Usuario o contraseña incorrectos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getById(id: Long): Result<User> {
        return try {
            val user = ds.findById(id)
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("Usuario no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
