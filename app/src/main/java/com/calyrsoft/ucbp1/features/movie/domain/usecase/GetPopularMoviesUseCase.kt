package com.calyrsoft.ucbp1.features.movie.domain.usecase

import com.calyrsoft.ucbp1.features.movie.domain.model.MovieModel
import com.calyrsoft.ucbp1.features.movie.domain.repository.IMoviesRepository

class GetPopularMoviesUseCase(
    private val repository: IMoviesRepository
) {
    suspend operator fun invoke(page: Int): Result<List<MovieModel>> {
       return repository.getPopular(page)
    }
}