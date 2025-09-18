package com.calyrsoft.ucbp1.data.repository

import com.calyrsoft.ucbp1.data.datasource.DollarLocalDataSource
import com.calyrsoft.ucbp1.data.datasource.RealTimeRemoteDataSource
import com.calyrsoft.ucbp1.data.error.DataException
import com.calyrsoft.ucbp1.domain.error.Failure
import com.calyrsoft.ucbp1.domain.model.DollarModel
import com.calyrsoft.ucbp1.domain.repository.IDollarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach


class DollarRepository(
    val realTimeRemoteDataSource: RealTimeRemoteDataSource,
    val localDataSource: DollarLocalDataSource
): IDollarRepository {


    override fun getDollarFromFireBaseInMyLocalDB(): Flow<DollarModel> {
//        return flow {
//            emit(DollarModel("123", "456"))
//        }
        return realTimeRemoteDataSource.getDollarUpdates().onEach {
                localDataSource.insert(it)
            }

//        return realTimeRemoteDataSource.getDollarUpdates()



    }

    override suspend fun getHistoryOfDollarsFromMyLocalDB(): Result<List<DollarModel>> {

        val history=  localDataSource.getList()
        return if (history != null) {
            Result.success(history)
        } else {

            Result.failure(Exception("Usuario o contraseña incorrectos"))
        }




    }

    override suspend fun deleteByTimestamp(timestamp: Long): Result<Unit> {
        return try {
            localDataSource.deleteByTimestamp(timestamp)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
