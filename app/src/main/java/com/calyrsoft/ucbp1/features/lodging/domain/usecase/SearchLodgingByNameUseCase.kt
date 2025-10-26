package com.calyrsoft.ucbp1.features.lodging.domain.usecase

import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import com.calyrsoft.ucbp1.features.lodging.domain.repository.ILodgingRepository
import kotlinx.coroutines.flow.Flow

class SearchLodgingByNameUseCase (private val repo: ILodgingRepository) {
    suspend operator fun invoke(name: String): Flow<List<Lodging>> {
        return repo.searchLodgingByName(name)
    }
}