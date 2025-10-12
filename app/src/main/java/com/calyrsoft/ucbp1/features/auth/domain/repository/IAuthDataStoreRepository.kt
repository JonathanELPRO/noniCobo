package com.calyrsoft.ucbp1.features.auth.domain.repository


interface IAuthDataStoreRepository {
    suspend fun saveUser(email: String,token:String,role:String)
    suspend fun getUserRole(): Result<String>
}