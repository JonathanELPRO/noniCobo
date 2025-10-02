package com.calyrsoft.ucbp1.features.movie.domain.repository

import com.calyrsoft.ucbp1.features.movie.domain.model.MovieModel

interface IMoviesRepository {
    suspend fun getPopular(page: Int): Result<List<MovieModel>>
    suspend fun insertMyFavoriteMovie(movieModel: MovieModel): Unit
    suspend fun getFavorites(): List<MovieModel>
}