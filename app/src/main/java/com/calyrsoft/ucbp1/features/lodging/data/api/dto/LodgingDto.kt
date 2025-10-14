package com.example.imperium_reality.features.register.data.api.dto

import com.calyrsoft.ucbp1.features.lodging.domain.model.RoomOption
import com.calyrsoft.ucbp1.features.lodging.domain.model.StayOption

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LodgingDto(
    @SerialName("name")
    val name: String,

    @SerialName("type")
    val type: String,

    @SerialName("district")
    val district: String? = null,

    @SerialName("address")
    val address: String? = null,

    @SerialName("contactPhone")
    val contactPhone: String? = null,

    @SerialName("open24h")
    val open24h: Boolean = false,

    @SerialName("ownerAdminId")
    val ownerAdminId: Long,

    @SerialName("latitude")
    val latitude: Double? = null,

    @SerialName("longitude")
    val longitude: Double? = null,

    @SerialName("stayOptions")
    val stayOptions: List<StayOption> = emptyList(),

    @SerialName("roomOptions")
    val roomOptions: List<RoomOption> = emptyList(),

    @SerialName("placeImageUri")
    val placeImageUri: String? = null,

    @SerialName("licenseImageUri")
    val licenseImageUri: String? = null
)
