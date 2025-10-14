package com.calyrsoft.ucbp1.features.auth.data.api

import com.example.ucbp1.features.login.data.api.dto.UserMetadataDto
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