package com.calyrsoft.ucbp1.domain.usecase

import com.calyrsoft.ucbp1.domain.model.DollarModel
import com.calyrsoft.ucbp1.domain.repository.IDollarRepository
import kotlinx.coroutines.flow.Flow

class GetDollarFromFireBaseInMyLocalDBUseCase(
    val repository: IDollarRepository
) {


    fun invoke(): Flow<DollarModel> {
        return repository.getDollarFromFireBaseInMyLocalDB()
    }
}
