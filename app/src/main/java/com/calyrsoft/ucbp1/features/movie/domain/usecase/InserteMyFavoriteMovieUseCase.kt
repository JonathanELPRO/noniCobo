package com.calyrsoft.ucbp1.features.movie.domain.usecase

import com.calyrsoft.ucbp1.features.movie.domain.model.MovieModel
import com.calyrsoft.ucbp1.features.movie.domain.repository.IMoviesRepository

class InserteMyFavoriteMovieUseCase(
    private val repository: IMoviesRepository
) {
    suspend operator fun invoke(movie: MovieModel): Unit {
        return repository.insertMyFavoriteMovie(movie)
    }
}