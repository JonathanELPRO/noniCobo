package com.calyrsoft.ucbp1.features.lodging.domain.usecase

import com.calyrsoft.ucbp1.features.lodging.domain.model.AddModel
import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import com.calyrsoft.ucbp1.features.lodging.domain.repository.ILodgingRepository
import kotlinx.coroutines.flow.Flow

class GetAddinRealTime(private val repo: ILodgingRepository) {
    operator  fun invoke(): Flow<AddModel> {
        return repo.getAddfromFirebase()
    }
}