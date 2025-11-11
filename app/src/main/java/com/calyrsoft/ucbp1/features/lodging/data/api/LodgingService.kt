package com.example.imperium_reality.features.register.data.api

import com.example.imperium_reality.features.register.data.api.dto.LodgingDto
import com.example.imperium_reality.features.register.data.api.dto.LodgingResponseDto
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface LodgingService {
    @POST("/rest/v1/lodgings")
    suspend fun addLodging(@Body lodgingRequestDto: LodgingDto): Response<Unit>

    @PATCH("/rest/v1/lodgings")
    suspend fun updateLodging(
        @Query("id") lodgingIdFilter: String = "eq.0",
        @Body lodgingDto: LodgingDto
    ): Response<Unit>

    @GET("/rest/v1/lodgings")
    suspend fun getLodgingDetail(@Query("select") sortBy: String = "*",
                                 @Query("id") id: String): Response<List<LodgingDto>>

    @GET("/rest/v1/lodgings")
    suspend fun getLodgingDetailByAdminId(@Query("select") select: String = "*",
                                 @Query("ownerAdminId") ownerAdminId: String): Response<List<LodgingResponseDto>>

    @GET("/rest/v1/lodgings")
    suspend fun getAllLodging(@Query("select") select: String = "*"): Response<List<LodgingResponseDto>>

    @GET("/rest/v1/lodgings")
    suspend fun searchLodgingsByName(@Query("select") select: String = "*",
                                          @Query("name") name: String): Response<List<LodgingResponseDto>>
    @GET("/rest/v1/lodgings")
    suspend fun searchLodgingsByNameAndAdminId(@Query("select") select: String = "*",
                                     @Query("name") name: String, @Query("ownerAdminId") ownerAdminId: String): Response<List<LodgingResponseDto>>

}