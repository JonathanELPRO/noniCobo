package com.calyrsoft.ucbp1.features.reservation.domain.usecase

import com.calyrsoft.ucbp1.features.reservation.domain.model.Payment
import com.calyrsoft.ucbp1.features.reservation.domain.model.PaymentType
import com.calyrsoft.ucbp1.features.reservation.domain.repository.IPaymentRepository

class RecordRemainingPaymentUseCase(private val repo: IPaymentRepository) {
    suspend operator fun invoke(reservationId: Long, amount: Double) {
        val payment = Payment(
            reservationId = reservationId,
            amount = amount,
            type = PaymentType.REMAINING,
            createdAt = System.currentTimeMillis()
        )
        repo.insert(payment)
    }
}
