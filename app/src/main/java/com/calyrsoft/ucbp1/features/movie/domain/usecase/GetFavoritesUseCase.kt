package com.calyrsoft.ucbp1.features.movie.domain.usecase

import com.calyrsoft.ucbp1.features.movie.domain.model.MovieModel
import com.calyrsoft.ucbp1.features.movie.domain.repository.IMoviesRepository

class GetFavoritesUseCase(
    private val repository: IMoviesRepository
) {
    suspend operator fun invoke(): List<MovieModel> {
        return repository.getFavorites()
    }
}