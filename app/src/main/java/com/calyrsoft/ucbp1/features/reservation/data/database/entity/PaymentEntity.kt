package com.calyrsoft.ucbp1.features.reservation.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "payments")
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val reservationId: Long,
    val amount: Double,
    val type: String,
    val createdAt: Long
)