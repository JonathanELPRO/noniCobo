package com.example.imperium_reality.features.register.data.api

import com.example.imperium_reality.features.register.data.api.dto.LodgingDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LodgingService {
    @POST("/auth/v1/signup")
    suspend fun register(@Body registerRequestDto: LodgingDto)
}