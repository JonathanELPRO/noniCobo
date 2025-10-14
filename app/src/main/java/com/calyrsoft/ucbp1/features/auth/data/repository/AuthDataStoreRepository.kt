package com.calyrsoft.ucbp1.features.auth.data.repository

import com.calyrsoft.ucbp1.dataStore.AuthDataStore
import com.calyrsoft.ucbp1.features.auth.domain.repository.IAuthDataStoreRepository


class AuthDataStoreRepository(val authDataStore: AuthDataStore
): IAuthDataStoreRepository {
    override suspend fun saveUser(email: String,token:String,role:String) {
        authDataStore.saveUser(email,token,role)
    }

    override suspend fun getUserRole(): Result<String> {
        return authDataStore.getRole()
    }


}