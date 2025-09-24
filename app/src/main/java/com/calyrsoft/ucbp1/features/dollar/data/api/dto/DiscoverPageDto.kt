package com.calyrsoft.ucbp1.features.dollar.data.api.dto
import com.google.gson.annotations.SerializedName

data class DiscoverPageDto(
    val page: Int,
    @SerializedName("results") val results: List<MovieDto>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)
