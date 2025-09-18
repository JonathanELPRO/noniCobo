package com.calyrsoft.ucbp1.domain.usecase

import com.calyrsoft.ucbp1.domain.model.DollarModel
import com.calyrsoft.ucbp1.domain.repository.IDollarRepository
import kotlinx.coroutines.flow.Flow


class GetHistoryOfDollarsFromMyLocalDBUseCase(
    val repository: IDollarRepository
) {

    suspend fun invoke(): Result<List<DollarModel>> {
        return repository.getHistoryOfDollarsFromMyLocalDB()
    }
}