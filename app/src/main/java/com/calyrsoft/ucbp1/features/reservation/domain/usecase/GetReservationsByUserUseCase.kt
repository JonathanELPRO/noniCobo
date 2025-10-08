package com.calyrsoft.ucbp1.features.reservation.domain.usecase

import com.calyrsoft.ucbp1.features.reservation.domain.model.Reservation
import com.calyrsoft.ucbp1.features.reservation.domain.repository.IReservationRepository
import kotlinx.coroutines.flow.Flow

class GetReservationsByUserUseCase(private val repo: IReservationRepository) {
    operator fun invoke(userId: Long): Flow<List<Reservation>> = repo.observeByUser(userId)
}
