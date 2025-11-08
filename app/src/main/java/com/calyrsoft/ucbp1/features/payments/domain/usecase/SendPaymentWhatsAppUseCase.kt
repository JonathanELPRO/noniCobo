package com.calyrsoft.ucbp1.features.payments.domain.usecase

import com.calyrsoft.ucbp1.features.payments.domain.model.PaymentModel
import com.calyrsoft.ucbp1.features.payments.domain.repository.IPaymentRepository

class SendPaymentWhatsAppUseCase(
    private val repository: IPaymentRepository
) {
    suspend operator fun invoke(payment: PaymentModel): Result<Unit> {
        return repository.sendPaymentViaWhatsApp(payment)
    }
}
