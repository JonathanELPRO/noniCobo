package com.calyrsoft.ucbp1.features.lodging.domain.model

data class Lodging(
    val id: Long? = null,
    val name: String,
    val type: LodgingType,
    val district: String?,
    val address: String?,
    val contactPhone: String?,
    val open24h: Boolean,
    val ownerAdminId: Long,
    val pricePerHour: Double?,
    val pricePerNight: Double?,
    val pricePerDay: Double?,
    val hasPrivateBathroom: Boolean,
    val hasSmartTV: Boolean,
    val latitude: Double?,
    val longitude: Double?,
    val roomTypes: List<RoomType> = emptyList()
)