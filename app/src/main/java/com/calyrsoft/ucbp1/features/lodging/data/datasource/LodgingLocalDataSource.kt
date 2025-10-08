package com.calyrsoft.ucbp1.features.lodging.data.datasource

import com.calyrsoft.ucbp1.features.lodging.data.database.dao.ILodgingDao
import com.calyrsoft.ucbp1.features.lodging.data.database.dao.IRoomTypeDao
import com.calyrsoft.ucbp1.features.lodging.data.database.entity.LodgingEntity
import com.calyrsoft.ucbp1.features.lodging.data.database.entity.RoomTypeEntity
import kotlinx.coroutines.flow.Flow



class LodgingLocalDataSource(
    private val lodgingDao: ILodgingDao,
    private val roomTypeDao: IRoomTypeDao
) {
    fun observeAll(): Flow<List<LodgingEntity>> = lodgingDao.observeAll()
    suspend fun findById(id: Long) = lodgingDao.findById(id)
    suspend fun upsertLodging(entity: LodgingEntity) = lodgingDao.upsert(entity)
    suspend fun findRoomTypes(lodgingId: Long) = roomTypeDao.findByLodging(lodgingId)
    suspend fun upsertRoomTypes(list: List<RoomTypeEntity>) = roomTypeDao.upsertAll(list)
}