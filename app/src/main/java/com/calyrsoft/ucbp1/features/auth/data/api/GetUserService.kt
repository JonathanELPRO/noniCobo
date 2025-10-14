package com.calyrsoft.ucbp1.features.auth.data.api

import com.example.imperium_reality.features.login.data.api.dto.UserDto
import com.example.imperium_reality.features.login.data.api.dto.UserMetadataDto
import com.example.imperium_reality.features.register.data.api.dto.RegisterRequestDto
import com.example.imperium_reality.features.register.data.api.dto.RegisterResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GetUserService {
    @GET("/rest/v1/users")
    suspend fun getByEmail(
        @Query("select") sortBy: String = "*",
        @Query("email") email: String
    ): Response<List<UserMetadataDto>>
}