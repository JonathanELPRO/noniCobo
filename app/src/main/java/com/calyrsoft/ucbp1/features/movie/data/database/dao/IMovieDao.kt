package com.calyrsoft.ucbp1.features.movie.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.calyrsoft.ucbp1.features.dollar.data.database.entity.DollarEntity
import com.calyrsoft.ucbp1.features.movie.data.database.entity.MovieEntity

@Dao
interface IMovieDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(lists: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    @Query("SELECT * FROM movies WHERE isFavorite = 1")
    suspend fun getFavorites(): List<MovieEntity>


}

//En un DAO defines qué operaciones quieres poder hacer con una tabla
//para ello necesirtas el entity que representa a un dato de la tabla
