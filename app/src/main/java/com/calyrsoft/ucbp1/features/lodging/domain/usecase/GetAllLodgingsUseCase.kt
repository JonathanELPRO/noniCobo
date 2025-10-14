package com.calyrsoft.ucbp1.features.lodging.domain.usecase

import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import com.calyrsoft.ucbp1.features.lodging.domain.repository.ILodgingRepository
import kotlinx.coroutines.flow.Flow

class GetAllLodgingsFromSupaBaseUseCase (private val repo: ILodgingRepository) {
    operator suspend fun invoke(): Flow<List<Lodging>> {
        return repo.getAllLodgings()
    }
}