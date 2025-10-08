package com.calyrsoft.ucbp1.features.reservation.domain.usecase

import com.calyrsoft.ucbp1.features.reservation.domain.model.Reservation
import com.calyrsoft.ucbp1.features.reservation.domain.repository.IReservationRepository

class CreateReservationUseCase(private val repo: IReservationRepository) {
    suspend operator fun invoke(reservation: Reservation): Long = repo.upsert(reservation)
}
