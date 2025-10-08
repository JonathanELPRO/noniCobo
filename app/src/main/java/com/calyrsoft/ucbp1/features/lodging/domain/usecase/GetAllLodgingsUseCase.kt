package com.calyrsoft.ucbp1.features.lodging.domain.usecase

import kotlinx.coroutines.flow.Flow
import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import com.calyrsoft.ucbp1.features.lodging.domain.repository.ILodgingRepository


class GetAllLodgingsUseCase(private val repo: ILodgingRepository) {
    operator fun invoke(): Flow<List<Lodging>> = repo.observeAll()
}