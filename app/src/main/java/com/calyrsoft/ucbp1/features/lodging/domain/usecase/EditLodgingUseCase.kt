package com.calyrsoft.ucbp1.features.lodging.domain.usecase

import com.calyrsoft.ucbp1.features.auth.domain.model.Role
import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import com.calyrsoft.ucbp1.features.lodging.domain.repository.ILodgingRepository

class EditLodgingUseCase (private val repo: ILodgingRepository) {
    suspend operator fun invoke(currentRole: Role, lodging: Lodging): Result<Unit> {
        require(currentRole == Role.ADMIN)
        return repo.editLodging(lodging.id,lodging)
    }
}