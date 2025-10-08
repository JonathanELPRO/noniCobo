package com.calyrsoft.ucbp1.features.reservation.domain.usecase

import com.calyrsoft.ucbp1.features.reservation.domain.model.Payment
import com.calyrsoft.ucbp1.features.reservation.domain.model.PaymentType
import com.calyrsoft.ucbp1.features.reservation.domain.repository.IPaymentRepository

class RecordAdvancePaymentUseCase(private val repo: IPaymentRepository) {
    suspend operator fun invoke(reservationId: Long, amount: Double) {
        val payment = Payment(
            reservationId = reservationId,
            amount = amount,
            type = PaymentType.ADVANCE,
            createdAt = System.currentTimeMillis()
        )
        repo.insert(payment)
    }
}
