package com.calyrsoft.ucbp1.features.auth.data.datasource

import android.util.Log
import com.calyrsoft.ucbp1.features.auth.data.api.GetUserService
import com.example.imperium_reality.features.login.data.api.dto.UserDto
import com.example.imperium_reality.features.login.data.api.dto.UserMetadataDto
import com.example.imperium_reality.features.register.data.error.DataException
import okhttp3.Response

class GetUserRemoteDataSource(val getUserService: GetUserService) {
    suspend fun getByEmail(email:String): Result<UserMetadataDto>{
        Log.d("GetUserRemoteDataSource", "Llegue")
        Log.d("GetUserRemoteDataSource", "Email: eq.$email")
        val response = getUserService.getByEmail(email="eq.$email")

        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                try {
//                    Log.d("GetUserRemoteDataSource", "Response: ${body.first()}")
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
}