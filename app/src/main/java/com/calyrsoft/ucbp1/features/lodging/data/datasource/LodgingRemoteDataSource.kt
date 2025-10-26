package com.calyrsoft.ucbp1.features.lodging.data.datasource

import android.util.Log
import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import com.example.imperium_reality.features.register.data.api.LodgingService
import com.example.imperium_reality.features.register.data.api.dto.LodgingDto
import com.example.imperium_reality.features.register.data.api.dto.LodgingResponseDto
import com.example.imperium_reality.features.register.data.error.DataException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow


class LodgingRemoteDataSource(
    private  val lodgingService: LodgingService,
) {
    suspend fun addLodging(lodgingDto: LodgingDto): Result<Unit> {
        Log.d("Request", "Sending lodging data: $lodgingDto")
        val response = lodgingService.addLodging(lodgingDto)
        Log.d("LodgingRemoteDataSource", "Response code: ${response.code()}")
        Log.d("LodgingRemoteDataSource", "Response message: ${response.message()}")
        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()
            Log.e("LodgingRemoteDataSource", "Error body: $errorBody")
        }

        return if (response.isSuccessful) {
            if (response.code() == 201) {
                // Creación exitosa, no hay body
                Result.success(Unit)
            } else {
                // Para otras respuestas exitosas con body, si existieran
                Result.success(Unit)
            }
        } else if (response.code() == 404) {
            Result.failure(DataException.HttpNotFound)
        } else {
            Result.failure(DataException.Unknown(response.message()))
        }
    }

    suspend fun getLodgingDetail(id:Long): Result<LodgingDto> {
        Log.d("LodgingRemoteDataSource", "Fetching lodging detail for ID: $id")
        val response = lodgingService.getLodgingDetail(id = "eq.${id}")
        Log.d("LodgingRemoteDataSource", "Response code: ${response.code()}")
        Log.d("LodgingRemoteDataSource", "Response message: ${response.message()}")
        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()
            Log.e("LodgingRemoteDataSource", "Error body: $errorBody")
        }

        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                try {
                    return  Result.success(body.first())
                } catch (e: Exception) {
                    return Result.failure(
                        DataException.Unknown(
                            e.message.toString()))
                }
            } else {
                return Result.failure(DataException.NoContent)
            }
        } else if (response.code() == 404) {
            return Result.failure(DataException.HttpNotFound)
        } else {
            return Result.failure(DataException.Unknown(response.message()))
        }
    }


    fun getAllLodgings(): Flow<List<LodgingResponseDto>> = flow {
        val response = lodgingService.getAllLodging()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                emit(body)
            } else {
                throw DataException.NoContent
            }
        } else if (response.code() == 404) {
            throw DataException.HttpNotFound
        } else {
            throw DataException.Unknown(response.message())
        }
    }.catch { e ->
        // opcional: puedes loggear el error aquí
        throw e
    }


    fun getLodgingsByAdmin(id: Long): Flow<List<LodgingResponseDto>> = flow {
        val response = lodgingService.getLodgingDetailByAdminId(ownerAdminId = "eq.$id")
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                emit(body) // emitimos directamente la lista
            } else {
                throw DataException.NoContent
            }
        } else if (response.code() == 404) {
            throw DataException.HttpNotFound
        } else {
            throw DataException.Unknown(response.message())
        }
    }.catch { e ->
        // opcional: puedes loggear el error aquí
        throw e
    }

    fun searchLodgingsByName(name: String): Flow<List<LodgingResponseDto>> = flow {
        val response = lodgingService.searchLodgingsByName(name = "ilike.%$name%")
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                emit(body) // emitimos directamente la lista
            } else {
                throw DataException.NoContent
            }
        } else if (response.code() == 404) {
            throw DataException.HttpNotFound
        } else {
            throw DataException.Unknown(response.message())
        }
    }.catch { e ->
        // opcional: puedes loggear el error aquí
        throw e
    }

    fun searchLodgingsByNameAndAdminId(name: String,id:Long): Flow<List<LodgingResponseDto>> = flow {
        val response = lodgingService.searchLodgingsByNameAndAdminId(name = "ilike.%$name%",ownerAdminId = "eq.$id")
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                emit(body) // emitimos directamente la lista
            } else {
                throw DataException.NoContent
            }
        } else if (response.code() == 404) {
            throw DataException.HttpNotFound
        } else {
            throw DataException.Unknown(response.message())
        }
    }.catch { e ->
        // opcional: puedes loggear el error aquí
        throw e
    }



}