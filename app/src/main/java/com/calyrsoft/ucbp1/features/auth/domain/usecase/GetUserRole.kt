package com.calyrsoft.ucbp1.features.auth.domain.usecase

import com.calyrsoft.ucbp1.features.auth.domain.repository.IAuthDataStoreRepository

class GetUserRole  (val loginDataStoreRepository: IAuthDataStoreRepository){
    suspend fun invoke(): Result<String>{
        return loginDataStoreRepository.getUserRole()
    }

}