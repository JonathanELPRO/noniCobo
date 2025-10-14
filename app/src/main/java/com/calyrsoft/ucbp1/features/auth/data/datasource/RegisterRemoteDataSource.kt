package com.calyrsoft.ucbp1.features.auth.data.datasource

import android.util.Log
import com.calyrsoft.ucbp1.features.auth.data.api.LoginService
import com.calyrsoft.ucbp1.features.auth.domain.model.Role
import com.example.imperium_reality.features.register.data.api.LodgingService
import com.example.imperium_reality.features.register.data.api.dto.RegisterUserDto
import com.example.imperium_reality.features.register.data.error.DataException
import kotlin.toString


class RegisterRemoteDataSource(
    val registerService: LodgingService,
    val loginService: LoginService
) {
    suspend fun login(email: String, password: String): Result<LoginResponseDto> {
        val response = loginService.login(LoginRequestDto(email,password))
        Log.d("LoginRemoteDataSource","Response: $response")
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                try {
                    return Result.success(response.body()!!)
                } catch (e: Exception) {
                    return Result.failure(
                        DataException.Unknown(
                            e.message.toString()))
                }
            } else {
                return Result.failure(DataException.NoContent)
            }
        } else if (response.code() == 404) {
            return Result.failure(DataException.HttpNotFound)
        } else {
            return Result.failure(DataException.Unknown(response.message()))
        }
    }

    suspend fun register(
        email: String, password: String,
        phone: String?,
        role: Role,
        username: String
    ): Result<RegisterResponseDto> {
        val response = registerService.register(
            RegisterRequestDto(
                email = email,
                password = password,
                data = RegisterUserDto(
                    phone = phone?:"",
                    username = username,
                    email=email,
                    role = role.toString()
                )
            )
        )
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                try {
                    return Result.success(response.body()!!)
                } catch (e: Exception) {
                    return Result.failure(
                        DataException.Unknown(
                            e.message.toString()))
                }
            } else {
                return Result.failure(DataException.NoContent)
            }
        } else if (response.code() == 404) {
            return Result.failure(DataException.HttpNotFound)
        } else {
            return Result.failure(DataException.Unknown(response.message()))
        }
    }
}