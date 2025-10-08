package com.calyrsoft.ucbp1.features.lodging.domain.usecase

import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import com.calyrsoft.ucbp1.features.lodging.domain.repository.ILodgingRepository

class GetLodgingDetailsUseCase(private val repo: ILodgingRepository) {
    suspend operator fun invoke(id: Long): Lodging? = repo.getDetails(id)
}