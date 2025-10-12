package com.calyrsoft.ucbp1.features.lodging.data.repository

import com.calyrsoft.ucbp1.features.lodging.data.datasource.LodgingLocalDataSource
import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import com.calyrsoft.ucbp1.features.lodging.domain.repository.ILodgingRepository
import kotlinx.coroutines.flow.Flow

class LodgingRepository(
    private val ds: LodgingLocalDataSource
) : ILodgingRepository {

    override fun observeAll(): Flow<List<Lodging>> {
        return ds.observeAll()
    }

    override suspend fun getDetails(id: Long): Result<Lodging> {
        val lodging = ds.findById(id)
        return if (lodging != null) {
            Result.success(lodging)
        } else {
            Result.failure(Exception("No se encontró el alojamiento con id=$id"))
        }
    }


    override suspend fun upsert(lodging: Lodging): Result<Unit> {
        return try {
            ds.upsertLodging(lodging)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
