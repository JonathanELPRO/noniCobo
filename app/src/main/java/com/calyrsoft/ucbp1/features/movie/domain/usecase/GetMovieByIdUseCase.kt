package com.calyrsoft.ucbp1.features.movie.domain.usecase

import com.calyrsoft.ucbp1.features.movie.domain.model.MovieModel
import com.calyrsoft.ucbp1.features.movie.domain.repository.IMoviesRepository

class GetMovieByIdUseCase(
    private val repository: IMoviesRepository
) {
    suspend operator fun invoke(id: Long): MovieModel? {
        return repository.getMovieById(id)
    }
}
