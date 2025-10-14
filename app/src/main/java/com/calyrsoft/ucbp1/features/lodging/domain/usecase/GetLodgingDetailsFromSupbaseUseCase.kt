package com.calyrsoft.ucbp1.features.lodging.domain.usecase

import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import com.calyrsoft.ucbp1.features.lodging.domain.repository.ILodgingRepository

class GetLodgingDetailsFromSupbaseUseCase(private val repo: ILodgingRepository) {
    operator suspend fun invoke(id: Long): Result<Lodging> {
        return repo.getLodgingDetails(id)
    }
}