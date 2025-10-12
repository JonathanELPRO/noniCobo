package com.calyrsoft.ucbp1.features.auth.data.repository

import android.util.Log
import com.calyrsoft.ucbp1.dataStore.AuthDataStore
import com.calyrsoft.ucbp1.features.auth.data.datasource.AuthLocalDataSource
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

    override suspend fun registerToSupabase(user:User,passwordPlain: String): Result<User>{
        if(user.email.isEmpty() or passwordPlain.isEmpty()) {
            return Result.failure(Exception("Credenciales Invalidos."))
        }

        val response = remoteDataSource.register(user.email,passwordPlain,user.phone,user.role,user.username)
        response.fold(
            onSuccess = {
                    it ->
                return Result.success(User(
                    email = it.email,
                    username = user.username,
                    phone = user.phone,
                    role = user.role,
                ))
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
            })
    }

    override suspend fun loginWithSupabase(email:String,password:String): Result<User>{
        if(email.isEmpty() or password.isEmpty()) {
            return Result.failure(Exception("Credenciales Invalidos."))
        }
        Log.d("LoginRepository","login: $email , $password")
        val response = remoteDataSource.login(email,password)
        response.fold(
            onSuccess = {
                    it ->
                authViewModel.saveUser(it.user.email, it.token, it.user.userMetadata.role)
                Log.d("ROL","${it.user.userMetadata.role}")
                Log.d("TOKEN","${it.token}")
                return Result.success(User(
                    email = it.user.email,
                    username = it.user.userMetadata.username,
                    phone = it.user.userMetadata.phone,
                    role = Role.valueOf(it.user.userMetadata.role.uppercase()),
                ))
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
            })
    }


}
