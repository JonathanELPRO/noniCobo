package com.calyrsoft.ucbp1.features.movie.data.datasource

import com.calyrsoft.ucbp1.features.movie.data.database.dao.IMovieDao
import com.calyrsoft.ucbp1.features.movie.data.mapper.toEntity
import com.calyrsoft.ucbp1.features.movie.domain.model.MovieModel

class MovieLocalDataSource(
    val dao: IMovieDao
) {


    suspend fun insertMovies(list: List<MovieModel>) {
        val moviesEntities = list.map { it.toEntity() }
        dao.insertMovies(moviesEntities)
    }



}
