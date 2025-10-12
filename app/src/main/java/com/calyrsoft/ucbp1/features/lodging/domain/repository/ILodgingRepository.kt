package com.calyrsoft.ucbp1.features.lodging.domain.repository

import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import kotlinx.coroutines.flow.Flow

interface ILodgingRepository {
    fun observeAll(): Flow<List<Lodging>>
    suspend fun getDetails(id: Long): Result<Lodging>
    suspend fun upsert(lodging: Lodging): Result<Unit>
}
