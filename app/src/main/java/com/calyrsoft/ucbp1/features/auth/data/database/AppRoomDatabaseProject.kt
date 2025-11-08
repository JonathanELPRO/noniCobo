package com.calyrsoft.ucbp1.features.auth.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.calyrsoft.ucbp1.features.auth.data.database.dao.IUserDao
import com.calyrsoft.ucbp1.features.auth.data.database.entity.UserEntity
import com.calyrsoft.ucbp1.features.lodging.data.database.dao.ILodgingDao
import com.calyrsoft.ucbp1.features.lodging.data.database.entity.Converters
import com.calyrsoft.ucbp1.features.lodging.data.database.entity.LodgingEntity


@Database(
    entities = [
        UserEntity::class,
        LodgingEntity::class,
    ],
    version = 7,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppRoomDatabaseProject : RoomDatabase() {

    abstract fun userDao(): IUserDao
    abstract fun lodgingDao(): ILodgingDao


    companion object {
        @Volatile
        private var Instance: AppRoomDatabaseProject? = null

        fun getDatabase(context: Context): AppRoomDatabaseProject {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppRoomDatabaseProject::class.java,
                    "mr_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
