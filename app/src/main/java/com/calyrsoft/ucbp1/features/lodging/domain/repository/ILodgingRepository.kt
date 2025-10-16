package com.calyrsoft.ucbp1.features.lodging.domain.repository

import com.calyrsoft.ucbp1.features.lodging.domain.model.AddModel
import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import kotlinx.coroutines.flow.Flow

interface ILodgingRepository {
    fun observeAll(): Flow<List<Lodging>>

    suspend fun getLodgingsByAdmin(id: Long): Flow<List<Lodging>>
    suspend fun getAllLodgings(): Flow<List<Lodging>>
    suspend fun getDetails(id: Long): Result<Lodging>
    suspend fun upsert(lodging: Lodging): Result<Unit>
    suspend fun addLodging(lodging: Lodging): Result<Unit>
    suspend fun getLodgingDetails(id: Long): Result<Lodging>

    fun getAddfromFirebase(): Flow<AddModel>
}
