package com.calyrsoft.ucbp1.features.movie.domain.model

data class MovieModel(
    val id: Long,
    val title: String?,
    val imageUrl: String? // URL completa para mostrar en la UI
)