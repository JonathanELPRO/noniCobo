package com.calyrsoft.ucbp1.features.reservation.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "reservations")
data class ReservationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val lodgingId: Long,
    val stayType: String,
    val hours: Int?,
    val startMillis: Long,
    val endMillis: Long,
    val status: String,
    val total: Double,
    val advancePaid: Double = 0.0
)