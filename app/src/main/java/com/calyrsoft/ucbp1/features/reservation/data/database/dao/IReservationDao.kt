package com.calyrsoft.ucbp1.features.reservation.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.calyrsoft.ucbp1.features.reservation.data.database.entity.ReservationEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface IReservationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: ReservationEntity): Long


    @Query("SELECT * FROM reservations WHERE userId = :userId ORDER BY startMillis DESC")
    fun observeByUser(userId: Long): Flow<List<ReservationEntity>>
}