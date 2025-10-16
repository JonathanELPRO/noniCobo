package com.calyrsoft.ucbp1.features.auth.data.repository

import android.util.Log
import com.calyrsoft.ucbp1.dataStore.AuthDataStore
import com.calyrsoft.ucbp1.features.auth.data.datasource.AuthLocalDataSource
import com.calyrsoft.ucbp1.features.auth.data.datasource.GetUserRemoteDataSource
import com.calyrsoft.ucbp1.features.auth.data.datasource.RegisterRemoteDataSource
import com.calyrsoft.ucbp1.features.auth.domain.model.Role
import com.calyrsoft.ucbp1.features.auth.domain.model.User
import com.calyrsoft.ucbp1.features.auth.domain.repository.IAuthRepository
import com.calyrsoft.ucbp1.features.auth.presentation.AuthViewModel
import com.example.imperium_reality.features.register.data.error.DataException
import com.example.imperium_reality.features.register.domain.error.Failure
import java.security.MessageDigest

class AuthRepository(
    private val ds: AuthLocalDataSource,
    private val remoteDataSource: RegisterRemoteDataSource,
    private val getUserRemoteDataSource: GetUserRemoteDataSource,
    private val authViewModel: AuthViewModel

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

            if (ds.existsByEmail(user.email.toString()) && ds.existsByUsername(user.username.toString()))
                return Result.failure(Exception("El correo electrónico y el nombre de usuario ya estan registrados"))

            if (ds.existsByUsername(user.username.toString()))
                return Result.failure(Exception("El nombre de usuario ya está en uso"))

            if (ds.existsByEmail(user.email.toString()))
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

    override suspend fun registerToSupabase(user: User, passwordPlain: String): Result<User> {

        if (user.email.toString().isEmpty() || passwordPlain.isEmpty()) {
            return Result.failure(Exception("Credenciales Invalidos."))
        }

        val response = remoteDataSource.register(
            email = user.email.toString(),
            password = passwordPlain,
            phone = user.phone?.toString(),
            role = user.role,
            username = user.username.toString()
        )
        response.fold(
            onSuccess = { it ->
                return Result.success(
                    User.create(
                        username = user.username.toString(),
                        email = it.email,
                        phone = user.phone?.toString(),
                        role = user.role.name
                    )
                )
            },
            onFailure = { exception ->
                val failure = when (exception) {
                    is DataException.Network -> Failure.NetworkConnection
                    is DataException.HttpNotFound -> Failure.NotFound
                    is DataException.NoContent -> Failure.EmptyBody
                    is DataException.Unknown -> Failure.Unknown(exception)
                    else -> Failure.Unknown(exception)
                }
                return Result.failure(failure)
            }
        )
    }

    override suspend fun loginWithSupabase(email: String, password: String): Result<User> {
        if (email.isEmpty() || password.isEmpty()) {
            return Result.failure(Exception("Credenciales Invalidos."))
        }
        Log.d("LoginRepository", "login: $email , $password")
        val response = remoteDataSource.login(email, password)
        response.fold(
            onSuccess = { it ->
                authViewModel.saveUser(it.user.email, it.token, it.user.userMetadata.role)
                Log.d("ROL", "${it.user.userMetadata.role}")
                Log.d("TOKEN", "${it.token}")

                return Result.success(
                    User.create(
                        id = it.user.id?.toLongOrNull(),
                        username = it.user.userMetadata.username,
                        email = it.user.email,
                        phone = it.user.userMetadata.phone,
                        role = it.user.userMetadata.role // Role.valueOf ocurre en create(...)
                    )
                )
            },
            onFailure = { exception ->
                val failure = when (exception) {
                    is DataException.Network -> Failure.NetworkConnection
                    is DataException.HttpNotFound -> Failure.NotFound
                    is DataException.NoContent -> Failure.EmptyBody
                    is DataException.Unknown -> Failure.Unknown(exception)
                    else -> Failure.Unknown(exception)
                }
                return Result.failure(failure)
            }
        )
    }

    override suspend fun getByEmail(email: String): Result<User> {
        if (email.isEmpty()) {
            return Result.failure(Exception("El email no puede ser vacio."))
        }
        val response = getUserRemoteDataSource.getByEmail(email)
        response.fold(
            onSuccess = { it ->
                return Result.success(
                    User.create(
                        id = it.id,
                        username = it.username,
                        email = it.email,
                        phone = it.phone,
                        role = it.role
                    )
                )
            },
            onFailure = { exception ->
                val failure = when (exception) {
                    is DataException.Network -> Failure.NetworkConnection
                    is DataException.HttpNotFound -> Failure.NotFound
                    is DataException.NoContent -> Failure.EmptyBody
                    is DataException.Unknown -> Failure.Unknown(exception)
                    else -> Failure.Unknown(exception)
                }
                return Result.failure(failure)
            }
        )
    }


}
