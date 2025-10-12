package com.calyrsoft.ucbp1.features.auth.domain.usecase

import com.calyrsoft.ucbp1.features.auth.domain.repository.IAuthDataStoreRepository

class SaveUserDataStore (val loginDataStoreRepository: IAuthDataStoreRepository){
    suspend fun invoke(email:String,token:String,role:String ){
        return loginDataStoreRepository.saveUser(email, token, role)
    }

}