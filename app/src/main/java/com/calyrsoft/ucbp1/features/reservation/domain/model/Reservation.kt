package com.calyrsoft.ucbp1.features.reservation.domain.model

data class Reservation(
    val id: Long? = null,
    val userId: Long,
    val lodgingId: Long,
    val stayType: StayType,
    val hours: Int?,
    val startMillis: Long,
    val endMillis: Long,
    val status: String,
    val total: Double,
    val advancePaid: Double
)

