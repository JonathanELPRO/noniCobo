package com.calyrsoft.ucbp1.features.reservation.domain.repository

import com.calyrsoft.ucbp1.features.reservation.domain.model.Payment


interface IPaymentRepository {
    suspend fun insert(payment: Payment): Long
    suspend fun sumByReservation(reservationId: Long): Double
}