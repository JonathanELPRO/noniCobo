package com.calyrsoft.ucbp1.features.lodging.domain.usecase

import com.calyrsoft.ucbp1.features.auth.domain.model.Role
import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import com.calyrsoft.ucbp1.features.lodging.domain.repository.ILodgingRepository

class UpsertLodgingUseCase(private val repo: ILodgingRepository) {
    suspend operator fun invoke(currentRole: Role, lodging: Lodging): Result<Unit> {
        require(currentRole == Role.ADMIN)
        //require(condición)	Verifica una condición antes de ejecutar algo.
        //Si la condición es verdadera	El código sigue ejecutándose normalmente.
        //Si la condición es falsa	Lanza IllegalArgumentException y corta la ejecución.
        return repo.upsert(lodging)
    }
}