package com.calyrsoft.ucbp1.features.dollar.domain.usecase

import com.calyrsoft.ucbp1.features.dollar.domain.repository.IDollarRepository

class DeleteByTimeStampUseCase(
    private val repository: IDollarRepository
) {
    suspend fun invoke(timestamp: Long): Result<Unit> {
        return repository.deleteByTimestamp(timestamp)
    }
}