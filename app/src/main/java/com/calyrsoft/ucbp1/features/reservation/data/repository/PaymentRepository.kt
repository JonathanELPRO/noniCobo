package com.calyrsoft.ucbp1.features.reservation.data.repository

import com.calyrsoft.ucbp1.features.reservation.data.datasource.ReservationLocalDataSource
import com.calyrsoft.ucbp1.features.reservation.data.database.entity.PaymentEntity
import com.calyrsoft.ucbp1.features.reservation.domain.model.Payment
import com.calyrsoft.ucbp1.features.reservation.domain.repository.IPaymentRepository


class PaymentRepository(private val ds: ReservationLocalDataSource) : IPaymentRepository {
    override suspend fun insert(payment: Payment): Long =
        ds.insertPayment(payment.toEntity())


    override suspend fun sumByReservation(reservationId: Long): Double =
        ds.sumPayments(reservationId)


    private fun Payment.toEntity() = PaymentEntity(
        id = id ?: 0,
        reservationId = reservationId,
        amount = amount,
        type = type.name,
        createdAt = createdAt
    )
}