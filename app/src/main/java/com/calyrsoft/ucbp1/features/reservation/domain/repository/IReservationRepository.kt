package com.calyrsoft.ucbp1.features.reservation.domain.repository

import com.calyrsoft.ucbp1.features.reservation.domain.model.Reservation
import kotlinx.coroutines.flow.Flow


interface IReservationRepository {
    suspend fun upsert(reservation: Reservation): Long
    fun observeByUser(userId: Long): Flow<List<Reservation>>
}