package com.calyrsoft.ucbp1.features.profile.data.repository

import com.calyrsoft.ucbp1.features.profile.data.datasource.LoginDataStore
import com.calyrsoft.ucbp1.features.profile.domain.model.*
import com.calyrsoft.ucbp1.features.profile.domain.repository.ILoginRepository

class LoginRepository(    private val dataStore: LoginDataStore
) : ILoginRepository {

    private val users = mutableListOf(
        LoginUserModel.create(
            name = "calyr",
            email = "calyr@gmail.com",
            password = "123456",
            phone = "12345678",
            imageUrl = "https://avatars.githubusercontent.com/u/874321?v=4",
            summary = "Esta es la descripción de calyr"
        ),
        LoginUserModel.create(
            name = "admin",
            email = "admin@gmail.com",
            password = "abcdef",
            phone = "87654321",
            imageUrl = "https://avatars.githubusercontent.com/u/874321?v=4",
            summary = "Esta es la descripcion del admin Jonathan"
        )
    )

    override fun findByNameAndPassword(name: String, password: String): Result<LoginUserModel> {
        val nameVO = Name.create(name)
        val passwordVO = Password.create(password)

        val user = users.find {
            it.name == nameVO && it.password == passwordVO
        }

        return user?.let { Result.success(it) }
            ?: Result.failure(Exception("Usuario o contraseña incorrectos"))
    }

    override fun findByName(name: String): Result<LoginUserModel> {
        val nameVO = Name.create(name)

        val user = users.find { it.name == nameVO }

        return user?.let { Result.success(it) }
            ?: Result.failure(Exception("Usuario o contraseña incorrectos"))
    }

    override fun updateUserProfile(
        name: String,
        newName: String?,
        newPhone: String?,
        newImageUrl: String?,
        newPassword: String?,
        newEmail: String?,
        newSummary: String?
    ): Result<LoginUserModel> {
        val nameVO = Name.create(name)

        val index = users.indexOfFirst { it.name == nameVO }
        if (index == -1) return Result.failure(Exception("Usuario no encontrado"))

        val user = users[index]
        val updatedUser = user.copy(
            name = newName?.let { Name.create(it) } ?: user.name,
            phone = newPhone?.let { Phone.create(it) } ?: user.phone,
            imageUrl = newImageUrl?.let { ImageUrl.create(it) } ?: user.imageUrl,
            password = newPassword?.let { Password.create(it) } ?: user.password,
            email = newEmail?.let { Email.create(it) } ?: user.email,
            summary = newSummary?.let { Summary.create(it) } ?: user.summary
        )

        users[index] = updatedUser
        return Result.success(updatedUser)
    }

    override suspend fun saveUserName(userName: String): Result<Unit> {
        return try {
            dataStore.saveUserName(userName)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserName(): Result<String> {
        return dataStore.getUserName()
    }

    override suspend fun saveToken(token: String): Result<Unit> {
        return try {
            dataStore.saveToken(token)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getToken(): Result<String> {
        return dataStore.getToken()
    }
}
