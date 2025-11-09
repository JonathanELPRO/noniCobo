package com.calyrsoft.ucbp1.features.profile.data.api

import com.calyrsoft.ucbp1.features.profile.data.api.dto.UpdateUserPasswordDto
import com.example.ucbp1.features.register.data.api.dto.UpdateUserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UpdateService {
    @PATCH("/rest/v1/users")
    suspend fun updateUser(
        @Query("id") id: String,
        @Body updateUserDto: UpdateUserDto
    ): Response<UpdateUserDto>

    @PUT("/auth/v1/user")
    suspend fun updateUserPassword(
        @Header("Authorization") auth: String,
        @Body updateUserPasswordDto: UpdateUserPasswordDto
    ): Response<UpdateUserPasswordDto>
}