package com.calyrsoft.ucbp1.features.lodging.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.calyrsoft.ucbp1.features.lodging.data.database.entity.LodgingEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ILodgingDao {
    @Query("SELECT * FROM lodgings ORDER BY id DESC")
    fun observeAll(): Flow<List<LodgingEntity>>


    @Query("SELECT * FROM lodgings WHERE id = :id")
    suspend fun findById(id: Long): LodgingEntity?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: LodgingEntity): Long
}