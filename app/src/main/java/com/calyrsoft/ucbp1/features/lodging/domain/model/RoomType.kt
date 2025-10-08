package com.calyrsoft.ucbp1.features.lodging.domain.model

data class RoomType(
    val name: String,
    val pricePerHour: Double?,
    val pricePerNight: Double?,
    val pricePerDay: Double?
)