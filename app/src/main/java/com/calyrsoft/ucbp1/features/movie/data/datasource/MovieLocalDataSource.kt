package com.calyrsoft.ucbp1.features.movie.data.datasource

import com.calyrsoft.ucbp1.features.movie.data.database.dao.IMovieDao
import com.calyrsoft.ucbp1.features.movie.data.mapper.toEntity
import com.calyrsoft.ucbp1.features.movie.data.mapper.toModel
import com.calyrsoft.ucbp1.features.movie.domain.model.MovieModel

class MovieLocalDataSource(
    val dao: IMovieDao
) {


    suspend fun insertMovies(list: List<MovieModel>) {
        val moviesEntities = list.map { it.toEntity() }
        dao.insertMovies(moviesEntities)
    }

    suspend fun insertMovie(movieModel: MovieModel) {
        val movieEntity = movieModel.toEntity()
        dao.insertMovie(movieEntity)
    }

    suspend fun getFavorites(): List<MovieModel> {
        return dao.getFavorites().map { it.toModel() }
    }



}
