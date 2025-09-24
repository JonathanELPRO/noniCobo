package com.calyrsoft.ucbp1.features.dollar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.calyrsoft.ucbp1.features.dollar.data.database.entity.DollarEntity

@Dao
interface IDollarDao {
    @Query("SELECT * FROM dollars")
    suspend fun getList(): List<DollarEntity>




    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(dollar: DollarEntity)




    @Query("DELETE FROM dollars")
    suspend fun deleteAll()




    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDollars(lists: List<DollarEntity>)

    @Query("DELETE FROM dollars WHERE timestamp = :timestamp")
    suspend fun deleteByTimestamp(timestamp: Long)
}

//En un DAO defines qué operaciones quieres poder hacer con una tabla
//para ello necesirtas el entity que representa a un dato de la tabla
