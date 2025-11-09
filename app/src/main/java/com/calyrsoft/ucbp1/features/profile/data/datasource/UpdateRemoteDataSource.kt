package com.calyrsoft.ucbp1.features.profile.data.datasource
import android.util.Log
import com.calyrsoft.ucbp1.features.profile.data.api.UpdateService
import com.calyrsoft.ucbp1.features.profile.data.api.dto.UpdateUserPasswordDto
import com.example.imperium_reality.features.register.data.error.DataException
import com.example.ucbp1.features.register.data.api.dto.UpdateUserDto
import kotlin.toString


class UpdateRemoteDataSource(
    val updateService: UpdateService
) {
    suspend fun updateUser(
        phone: String,
        username: String,
        id: Long
    ): Result<Unit>  {
        val newUser= UpdateUserDto(
            username = username,
            phone = phone
        )
        Log.d("Request", "Sending update data: $newUser")
        val response =updateService.updateUser(id="eq.${id}",
            newUser
           )
        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()
            Log.e("UpdatingRemoteDataSource", "Error body: $errorBody")
        }
        return if (response.isSuccessful) {
            if (response.code() == 204) {
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
    suspend fun updateUserPassword(
        newPassword:String,
        token:String
    ): Result<Unit>  {
        val response =updateService.updateUserPassword("Bearer $token",
            UpdateUserPasswordDto(newPassword)
        )
        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()
        }
        return if (response.isSuccessful) {
            if (response.code() == 204) {
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
}