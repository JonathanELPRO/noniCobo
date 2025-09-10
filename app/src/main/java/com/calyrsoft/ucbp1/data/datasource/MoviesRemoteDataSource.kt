package com.calyrsoft.ucbp1.data.datasource
import com.calyrsoft.ucbp1.data.api.TmdbService
import com.calyrsoft.ucbp1.data.api.dto.DiscoverPageDto
import com.calyrsoft.ucbp1.data.api.dto.MovieDto

class MoviesRemoteDataSource(
    private val service: TmdbService
) {
    suspend fun getPopularMovies(page: Int): Result<DiscoverPageDto> {
        val response = service.discoverMovies(page = page)
        return if (response.isSuccessful) {
            Result.success(response.body()!!)
        } else {
            Result.failure(Exception("Error al obtener la pagina de peliculas"))
        }
    }
}
