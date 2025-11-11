package com.calyrsoft.ucbp1.features.profile.domain.repository

interface IUpdateRepository {
    suspend fun updateUser(id:Long,username:String,phone:String): Result<Unit>
    suspend fun updateUserPassword(newPassword:String,token:String): Result<Unit>

}