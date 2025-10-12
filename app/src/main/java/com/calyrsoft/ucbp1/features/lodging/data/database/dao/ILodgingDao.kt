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
    //se llama upsert porque inserta y actualiza, eso lo hace con .REPLACE
    //que dice en cuanto trates de insertar algo si ves que ya existia otro algo con ese mismo id
    //solo reemplazalo(aqui en realidad estarias actualizando)
}
