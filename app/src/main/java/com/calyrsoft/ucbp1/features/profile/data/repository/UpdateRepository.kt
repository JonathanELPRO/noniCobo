package com.calyrsoft.ucbp1.features.profile.data.repository

import com.calyrsoft.ucbp1.features.profile.data.datasource.UpdateRemoteDataSource
import com.calyrsoft.ucbp1.features.profile.domain.repository.IUpdateRepository

class UpdateRepository(
    private val remoteDataSource: UpdateRemoteDataSource,
) : IUpdateRepository{
    override suspend fun updateUser(id:Long,username:String,phone:String): Result<Unit>{
        return try {
            remoteDataSource.updateUser(phone,username,id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUserPassword(newPassword:String,token:String): Result<Unit>{
        return try {
            remoteDataSource.updateUserPassword(newPassword,token)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}