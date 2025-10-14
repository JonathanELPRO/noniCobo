package com.calyrsoft.ucbp1.features.movie.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MovieModel(
    val id: Long,
    val title: String?,
    val imageUrl: String?,
    val isFavorite: Boolean = false
)