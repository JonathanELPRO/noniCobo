package com.calyrsoft.ucbp1.domain.repository
import com.calyrsoft.ucbp1.domain.model.MovieModel

interface IMoviesRepository {
    suspend fun getPopular(page: Int): Result<List<MovieModel>>
}
