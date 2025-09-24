package com.calyrsoft.ucbp1.features.dollar.domain.usecase

import com.calyrsoft.ucbp1.features.dollar.domain.model.DollarModel
import com.calyrsoft.ucbp1.features.dollar.domain.repository.IDollarRepository
import kotlinx.coroutines.flow.Flow

class GetDollarFromFireBaseInMyLocalDBUseCase(
    val repository: IDollarRepository
) {


    fun invoke(): Flow<DollarModel> {
        return repository.getDollarFromFireBaseInMyLocalDB()
    }
}
