package com.calyrsoft.ucbp1.features.lodging.data.repository

import android.util.Log
import com.calyrsoft.ucbp1.features.lodging.data.datasource.LodgingLocalDataSource
import com.calyrsoft.ucbp1.features.lodging.data.datasource.LodgingRemoteDataSource
import com.calyrsoft.ucbp1.features.lodging.data.datasource.RealTimeRemoteDataSource2
import com.calyrsoft.ucbp1.features.lodging.domain.model.AddModel
import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import com.calyrsoft.ucbp1.features.lodging.domain.model.LodgingType
import com.calyrsoft.ucbp1.features.lodging.domain.repository.ILodgingRepository
import com.example.imperium_reality.features.register.data.api.dto.LodgingDto
import com.example.imperium_reality.features.register.data.error.DataException
import com.example.imperium_reality.features.register.domain.error.Failure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class LodgingRepository(
    private val ds: LodgingLocalDataSource,
    private val remoteDataSource: LodgingRemoteDataSource,
    private val realTimeRemoteDataSource: RealTimeRemoteDataSource2
) : ILodgingRepository {

    override fun getAddfromFirebase(): Flow<AddModel>{
        return realTimeRemoteDataSource.getAddsUpdate()
    }

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

    override suspend fun getLodgingDetails(id: Long): Result<Lodging> {
        val response = remoteDataSource.getLodgingDetail(id)
        response.fold(
            onSuccess = {
                    it ->

                return Result.success(Lodging(
                    id = it.id,
                    name = it.name,
                    type = LodgingType.valueOf(it.type),
                    address = it.address,
                    contactPhone = it.contactPhone,
                    open24h = it.open24h,
                    ownerAdminId = it.ownerAdminId,
                    latitude = it.latitude,
                    longitude = it.longitude,
                    stayOptions = it.stayOptions,
                    roomOptions = it.roomOptions,
                    placeImageUri = it.placeImageUri,
                    licenseImageUri = it.licenseImageUri
                ))
            },
            onFailure = { exception ->
                val failure = when (exception) {
                    is DataException.Network -> Failure.NetworkConnection
                    is DataException.HttpNotFound -> Failure.NotFound
                    is DataException.NoContent -> Failure.EmptyBody
                    is DataException.Unknown -> Failure.Unknown(exception)
                    else -> Failure.Unknown(exception)
                }
                return Result.failure(failure)
            })
    }


    override suspend fun upsert(lodging: Lodging): Result<Unit> {
        return try {
            ds.upsertLodging(lodging)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun editLodging (id:Long?,lodging: Lodging): Result<Unit> {
        return try {
            Log.d("LodgingRemoteDataSource", "Editing lodging with ID: $id")

            remoteDataSource.editLodging(id,
                LodgingDto(
                    id=lodging.id,
                    lodging.name,
                    lodging.type.name,
                    lodging.address,
                    lodging.address,
                    lodging.contactPhone,
                    lodging.open24h,
                    lodging.ownerAdminId,
                    lodging.latitude,
                    lodging.longitude,
                    lodging.stayOptions,
                    lodging.roomOptions,
                    lodging.placeImageUri,
                    lodging.licenseImageUri
                )

            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override suspend fun addLodging (lodging: Lodging): Result<Unit> {
        return try {

            remoteDataSource.addLodging(
                LodgingDto(
                    id=null,
                lodging.name,
                lodging.type.name,
                lodging.address,
                lodging.address,
                lodging.contactPhone,
                lodging.open24h,
                lodging.ownerAdminId,
                    lodging.latitude,
                    lodging.longitude,
                lodging.stayOptions,
                lodging.roomOptions,
                    lodging.placeImageUri,
                    lodging.licenseImageUri
                )

            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getLodgingsByAdmin(id: Long): Flow<List<Lodging>> {
        return remoteDataSource.getLodgingsByAdmin(id).map { dtoList ->
            dtoList.map { dto ->
                Lodging(
                    id = dto.id ?: 0L,
                    name = dto.name,
                    type = LodgingType.valueOf(dto.type),
                    address = dto.address,
                    contactPhone = dto.contactPhone,
                    open24h = dto.open24h,
                    ownerAdminId = dto.ownerAdminId,
                    latitude = dto.latitude,
                    longitude = dto.longitude,
                    stayOptions = dto.stayOptions,
                    roomOptions = dto.roomOptions,
                    placeImageUri = dto.placeImageUri,
                    licenseImageUri = dto.licenseImageUri
                )
            }
        }
    }

    override suspend fun getAllLodgings(): Flow<List<Lodging>> {
        return remoteDataSource.getAllLodgings().map { dtoList ->
            dtoList.map { dto ->
                Lodging(
                    id = dto.id ?: 0L,
                    name = dto.name,
                    type = LodgingType.valueOf(dto.type),
                    address = dto.address,
                    contactPhone = dto.contactPhone,
                    open24h = dto.open24h,
                    ownerAdminId = dto.ownerAdminId,
                    latitude = dto.latitude,
                    longitude = dto.longitude,
                    stayOptions = dto.stayOptions,
                    roomOptions = dto.roomOptions,
                    placeImageUri = dto.placeImageUri,
                    licenseImageUri = dto.licenseImageUri
                )
            }
        }
    }

    override suspend fun searchLodgingByName(name: String): Flow<List<Lodging>> {
        return remoteDataSource.searchLodgingsByName(name).map { dtoList ->
            dtoList.map { dto ->
                Lodging(
                    id = dto.id ?: 0L,
                    name = dto.name,
                    type = LodgingType.valueOf(dto.type),
                    address = dto.address,
                    contactPhone = dto.contactPhone,
                    open24h = dto.open24h,
                    ownerAdminId = dto.ownerAdminId,
                    latitude = dto.latitude,
                    longitude = dto.longitude,
                    stayOptions = dto.stayOptions,
                    roomOptions = dto.roomOptions,
                    placeImageUri = dto.placeImageUri,
                    licenseImageUri = dto.licenseImageUri
                )
            }
        }
    }

    override suspend fun searchLodgingByNameAndAdminId(name: String,id:Long): Flow<List<Lodging>> {
        return remoteDataSource.searchLodgingsByNameAndAdminId(name,id).map { dtoList ->
            dtoList.map { dto ->
                Lodging(
                    id = dto.id ?: 0L,
                    name = dto.name,
                    type = LodgingType.valueOf(dto.type),
                    address = dto.address,
                    contactPhone = dto.contactPhone,
                    open24h = dto.open24h,
                    ownerAdminId = dto.ownerAdminId,
                    latitude = dto.latitude,
                    longitude = dto.longitude,
                    stayOptions = dto.stayOptions,
                    roomOptions = dto.roomOptions,
                    placeImageUri = dto.placeImageUri,
                    licenseImageUri = dto.licenseImageUri
                )
            }
        }
    }


}
