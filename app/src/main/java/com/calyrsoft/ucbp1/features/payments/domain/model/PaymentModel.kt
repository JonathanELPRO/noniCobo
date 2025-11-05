package com.calyrsoft.ucbp1.features.payments.domain.model

import com.calyrsoft.ucbp1.features.lodging.domain.model.RoomCategory
import com.calyrsoft.ucbp1.features.lodging.domain.model.StayType
import kotlinx.serialization.Serializable

@Serializable
data class PaymentModel(
    val lodgingName: String,
    val userName: String = "Usuario Demo", // valor harcodeado temporal
    val selectedRoom: RoomCategory?,
    val selectedStay: StayType?,
    val selectedPrice: Double,
    val arrivalTime: String,
    val hours: String?,
    val ownerNumber: String?,
)
