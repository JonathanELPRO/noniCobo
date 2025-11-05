package com.calyrsoft.ucbp1.features.payments.domain.repository

import com.calyrsoft.ucbp1.features.payments.domain.model.PaymentModel

interface IPaymentRepository {
    suspend fun sendPaymentViaWhatsApp(payment: PaymentModel): Result<Unit>
}
