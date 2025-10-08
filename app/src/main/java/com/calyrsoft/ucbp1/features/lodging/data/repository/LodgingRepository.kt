package com.calyrsoft.ucbp1.features.lodging.data.repository

import com.calyrsoft.ucbp1.features.lodging.data.datasource.LodgingLocalDataSource
import com.calyrsoft.ucbp1.features.lodging.data.database.entity.LodgingEntity
import com.calyrsoft.ucbp1.features.lodging.domain.model.*
import com.calyrsoft.ucbp1.features.lodging.domain.repository.ILodgingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LodgingRepository(private val ds: LodgingLocalDataSource) : ILodgingRepository {

    override fun observeAll(): Flow<List<Lodging>> =
        ds.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun getDetails(id: Long): Lodging? =
        ds.findById(id)?.toDomain()

    override suspend fun upsert(lodging: Lodging): Long =
        ds.upsertLodging(lodging.toEntity())

    // 🔁 Mappers
    private fun LodgingEntity.toDomain() = Lodging(
        id = id,
        name = name,
        type = LodgingType.valueOf(type),
        district = district,
        address = address,
        contactPhone = contactPhone,
        open24h = open24h,
        ownerAdminId = ownerAdminId,
        latitude = latitude,
        longitude = longitude,
        stayOptions = stayOptions,
        roomOptions = roomOptions,
        placeImage = placeImage,
        licenseImage = licenseImage
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
        latitude = latitude,
        longitude = longitude,
        stayOptions = stayOptions,
        roomOptions = roomOptions,
        placeImage = placeImage,
        licenseImage = licenseImage
    )
}
