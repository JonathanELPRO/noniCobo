package com.calyrsoft.ucbp1.features.movie.data.api

import com.calyrsoft.ucbp1.features.movie.data.api.dto.DiscoverPageDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbService {
    @GET("discover/movie")
    suspend fun discoverMovies(
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("api_key") apiKey: String = "fa3e844ce31744388e07fa47c7c5d8c3",
        @Query("page") page: Int,
        @Query("language") language: String = "es-ES"): Response<DiscoverPageDto>
}