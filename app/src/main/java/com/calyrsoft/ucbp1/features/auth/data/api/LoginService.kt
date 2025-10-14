package com.calyrsoft.ucbp1.features.auth.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface LoginService {
    @POST("/auth/v1/token?grant_type=password")
    suspend fun login(@Body loginRequest: LoginRequestDto): Response<LoginResponseDto>
}