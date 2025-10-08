package com.calyrsoft.ucbp1.features.lodging.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "lodgings")
data class LodgingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: String,
    val district: String?,
    val address: String?,
    val contactPhone: String?,
    val open24h: Boolean = false,
    val ownerAdminId: Long,
    val pricePerHour: Double?,
    val pricePerNight: Double?,
    val pricePerDay: Double?,
    val hasPrivateBathroom: Boolean = false,
    val hasSmartTV: Boolean = false,
    val latitude: Double? = null,
    val longitude: Double? = null
)