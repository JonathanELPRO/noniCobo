package com.calyrsoft.ucbp1.features.dollar.domain.usecase

import com.calyrsoft.ucbp1.features.dollar.domain.model.DollarModel
import com.calyrsoft.ucbp1.features.dollar.domain.repository.IDollarRepository


class GetHistoryOfDollarsFromMyLocalDBUseCase(
    val repository: IDollarRepository
) {

    suspend fun invoke(): Result<List<DollarModel>> {
        return repository.getHistoryOfDollarsFromMyLocalDB()
    }
}