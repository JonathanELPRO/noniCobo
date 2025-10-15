package com.calyrsoft.ucbp1.features.lodging.domain.usecase

import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import com.calyrsoft.ucbp1.features.lodging.domain.repository.ILodgingRepository
import kotlinx.coroutines.flow.Flow

class ObserveAllLocalLodgingsUseCase(
    private val repository: ILodgingRepository
) {
    suspend operator fun invoke(): Flow<List<Lodging>> {
        return repository.observeAll()
    }
}
