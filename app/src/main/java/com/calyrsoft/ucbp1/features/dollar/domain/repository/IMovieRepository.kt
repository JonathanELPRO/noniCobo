package com.calyrsoft.ucbp1.features.dollar.domain.repository
import com.calyrsoft.ucbp1.features.dollar.domain.model.MovieModel

interface IMoviesRepository {
    suspend fun getPopular(page: Int): Result<List<MovieModel>>
}
