package com.calyrsoft.ucbp1.features.lodging.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.calyrsoft.ucbp1.features.lodging.data.database.entity.RoomTypeEntity


@Dao
interface IRoomTypeDao {
    @Query("SELECT * FROM room_types WHERE lodgingId = :lodgingId")
    suspend fun findByLodging(lodgingId: Long): List<RoomTypeEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(types: List<RoomTypeEntity>)
}