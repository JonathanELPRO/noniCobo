package com.calyrsoft.ucbp1.features.lodging.domain.model

data class Lodging(
    val id: Long? = null,
    val name: String,
    val type: LodgingType,
    val district: String? = null,
    val address: String? = null,
    val contactPhone: String? = null,
    val open24h: Boolean = false,
    val ownerAdminId: Long,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val stayOptions: List<StayOption> = emptyList(),
    val roomOptions: List<RoomOption> = emptyList(),
    val placeImage: ByteArray? = null,
    val licenseImage: ByteArray? = null
)

