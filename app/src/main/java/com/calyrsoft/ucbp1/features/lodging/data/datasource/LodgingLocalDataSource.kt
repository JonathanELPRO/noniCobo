package com.calyrsoft.ucbp1.features.lodging.data.datasource

import com.calyrsoft.ucbp1.features.lodging.data.database.dao.ILodgingDao
import com.calyrsoft.ucbp1.features.lodging.data.database.entity.LodgingEntity
import kotlinx.coroutines.flow.Flow

class LodgingLocalDataSource(
    private val lodgingDao: ILodgingDao
) {
    fun observeAll(): Flow<List<LodgingEntity>> = lodgingDao.observeAll()
    suspend fun findById(id: Long) = lodgingDao.findById(id)
    suspend fun upsertLodging(entity: LodgingEntity) = lodgingDao.upsert(entity)
}
