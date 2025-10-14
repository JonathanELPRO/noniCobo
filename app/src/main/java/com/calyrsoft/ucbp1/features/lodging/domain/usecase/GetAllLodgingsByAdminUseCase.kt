package com.calyrsoft.ucbp1.features.lodging.domain.usecase

import kotlinx.coroutines.flow.Flow
import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import com.calyrsoft.ucbp1.features.lodging.domain.repository.ILodgingRepository


class GetAllLodgingsByAdminUseCase(private val repo: ILodgingRepository) {
    operator suspend fun invoke(id: Long): Flow<List<Lodging>> {
        return repo.getLodgingsByAdmin(id)
    }
}