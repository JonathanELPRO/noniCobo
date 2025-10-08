package com.calyrsoft.ucbp1.features.lodging.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "room_types")
data class RoomTypeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val lodgingId: Long,
    val name: String,
    val pricePerHour: Double?,
    val pricePerNight: Double?,
    val pricePerDay: Double?
)