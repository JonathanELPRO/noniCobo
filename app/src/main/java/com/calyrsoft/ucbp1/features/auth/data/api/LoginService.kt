package com.calyrsoft.ucbp1.features.auth.data.api

import com.example.ucbp1.features.login.data.api.dto.LoginRequestDto
import com.example.ucbp1.features.login.data.api.dto.LoginResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface LoginService {
    @POST("/auth/v1/token?grant_type=password")
//    /auth/v1/token → ruta del servicio de autenticación de Supabase.
//
//    ?grant_type=password → indica el tipo de autenticación que estás usando (en este caso, usuario + contraseña).
    suspend fun login(@Body loginRequest: LoginRequestDto): Response<LoginResponseDto>
}