package com.calyrsoft.ucbp1.features.lodging.domain.usecase

import com.calyrsoft.ucbp1.features.auth.domain.model.Role
import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import com.calyrsoft.ucbp1.features.lodging.domain.repository.ILodgingRepository

class UpsertLodgingUseCase(private val repo: ILodgingRepository) {
    suspend operator fun invoke(lodging: Lodging): Result<Unit> {
        return repo.upsert(lodging)
    }
}