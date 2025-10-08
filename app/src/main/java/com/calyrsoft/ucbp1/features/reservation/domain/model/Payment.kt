package com.calyrsoft.ucbp1.features.reservation.domain.model

data class Payment(
    val id: Long? = null,
    val reservationId: Long,
    val amount: Double,
    val type: PaymentType,
    val createdAt: Long
)

