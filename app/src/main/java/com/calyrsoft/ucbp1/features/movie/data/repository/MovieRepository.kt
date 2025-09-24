package com.calyrsoft.ucbp1.features.movie.data.repository

import com.calyrsoft.ucbp1.features.movie.domain.model.MovieModel
import com.calyrsoft.ucbp1.features.movie.domain.repository.IMoviesRepository
import com.calyrsoft.ucbp1.features.movie.data.datasource.MoviesRemoteDataSource

private const val TMDB_IMAGE_BASE_W185 = "https://image.tmdb.org/t/p/w185"

class MoviesRepository(
    private val remote: MoviesRemoteDataSource
) : IMoviesRepository {

    override suspend fun getPopular(page: Int): Result<List<MovieModel>> {
        val response = remote.getPopularMovies(page)
        response.fold(
            onSuccess = { page ->
                var dtos = page.results
                var models = dtos.map { dto ->
                    MovieModel(
                        id = dto.id,
                        title = dto.title,
                        imageUrl = dto.backdropPath?.let { "$TMDB_IMAGE_BASE_W185$it" }
                    )
                }
                return Result.success(models)
            },
            onFailure = { exception -> return Result.failure(exception) }
        )
    }
}