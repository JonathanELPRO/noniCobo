package com.calyrsoft.ucbp1.features.dollar.data.api.dto
import com.google.gson.annotations.SerializedName

data class MovieDto(
    val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("poster_path") val posterPath: String?
)
