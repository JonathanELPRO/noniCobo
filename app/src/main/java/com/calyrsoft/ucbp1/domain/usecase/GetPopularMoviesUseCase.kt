package com.calyrsoft.ucbp1.domain.usecase
import com.calyrsoft.ucbp1.domain.model.MovieModel
import com.calyrsoft.ucbp1.domain.repository.IMoviesRepository

class GetPopularMoviesUseCase(
    private val repository: IMoviesRepository
) {
    suspend operator fun invoke(page: Int): Result<List<MovieModel>> {
       return repository.getPopular(page)
    }
}
