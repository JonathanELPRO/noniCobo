package com.calyrsoft.ucbp1.features.movie.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.calyrsoft.ucbp1.features.dollar.data.database.dao.IDollarDao
import com.calyrsoft.ucbp1.features.dollar.data.database.entity.DollarEntity
import com.calyrsoft.ucbp1.features.movie.data.database.dao.IMovieDao
import com.calyrsoft.ucbp1.features.movie.data.database.entity.MovieEntity

@Database(entities = [MovieEntity::class], version = 6)
abstract class AppRoomDatabaseMovies : RoomDatabase() {
    abstract fun movieDao(): IMovieDao



    companion object {
        @Volatile
        private var Instance: AppRoomDatabaseMovies? = null




        fun getDatabase(context: Context): AppRoomDatabaseMovies {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppRoomDatabaseMovies::class.java,
                    "movie_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
