package com.calyrsoft.ucbp1.features.lodging.domain.usecase

import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import com.calyrsoft.ucbp1.features.lodging.domain.repository.ILodgingRepository
import kotlinx.coroutines.flow.Flow

class SearchByNameAndAdminIdUseCase (private val repo: ILodgingRepository) {
    suspend operator fun invoke(name: String,id:Long): Flow<List<Lodging>> {
        return repo.searchLodgingByNameAndAdminId(name,id)
    }
}