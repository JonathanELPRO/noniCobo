package com.calyrsoft.ucbp1.features.lodging.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.calyrsoft.ucbp1.features.lodging.data.datasource.LodgingLocalDataSource
import com.calyrsoft.ucbp1.features.lodging.data.database.entity.LodgingEntity
import com.calyrsoft.ucbp1.features.lodging.data.database.entity.RoomTypeEntity
import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import com.calyrsoft.ucbp1.features.lodging.domain.model.LodgingType
import com.calyrsoft.ucbp1.features.lodging.domain.model.RoomType
import com.calyrsoft.ucbp1.features.lodging.domain.repository.ILodgingRepository


class LodgingRepository(private val ds: LodgingLocalDataSource) : ILodgingRepository {
    override fun observeAll(): Flow<List<Lodging>> = ds.observeAll().map { list ->
        list.map { it.toDomain(emptyList()) }
    }


    override suspend fun getDetails(id: Long): Lodging? {
        val e = ds.findById(id) ?: return null
        val r = ds.findRoomTypes(id)
        return e.toDomain(r)
    }


    override suspend fun upsert(lodging: Lodging): Long {
        val id = ds.upsertLodging(lodging.toEntity())
        if (lodging.roomTypes.isNotEmpty()) {
            val roomTypeEntities: List<RoomTypeEntity> = lodging.roomTypes.map { roomType -> roomType.toEntity(id) }
            ds.upsertRoomTypes(roomTypeEntities)
        }
        return id
    }


    private fun LodgingEntity.toDomain(roomTypes: List<RoomTypeEntity>) = Lodging(
        id = id,
        name = name,
        type = LodgingType.valueOf(type),
        district = district,
        address = address,
        contactPhone = contactPhone,
        open24h = open24h,
        ownerAdminId = ownerAdminId,
        pricePerHour = pricePerHour,
        pricePerNight = pricePerNight,
        pricePerDay = pricePerDay,
        hasPrivateBathroom = hasPrivateBathroom,
        hasSmartTV = hasSmartTV,
        latitude = latitude,
        longitude = longitude,
        roomTypes = roomTypes.map { RoomType(it.name, it.pricePerHour, it.pricePerNight, it.pricePerDay) }
    )


    private fun Lodging.toEntity() = LodgingEntity(
        id = id ?: 0,
        name = name,
        type = type.name,
        district = district,
        address = address,
        contactPhone = contactPhone,
        open24h = open24h,
        ownerAdminId = ownerAdminId,
        pricePerHour = pricePerHour,
        pricePerNight = pricePerNight,
        pricePerDay = pricePerDay,
        hasPrivateBathroom = hasPrivateBathroom,
        hasSmartTV = hasSmartTV,
        latitude = latitude,
        longitude = longitude
    )


    private fun RoomType.toEntity(lodgingId: Long) = RoomTypeEntity(
        lodgingId = lodgingId,
        name = name,
        pricePerHour = pricePerHour,
        pricePerNight = pricePerNight,
        pricePerDay = pricePerDay
    )
}