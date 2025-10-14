package com.calyrsoft.ucbp1.features.lodging.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.calyrsoft.ucbp1.features.lodging.domain.model.RoomOption
import com.calyrsoft.ucbp1.features.lodging.domain.model.StayOption

import androidx.room.*
import com.calyrsoft.ucbp1.features.auth.data.database.entity.UserEntity

@Entity(
    tableName = "lodgings"
)
@TypeConverters(Converters::class)
data class LodgingEntity(
    @PrimaryKey() val id: Long,
    val name: String,
    val type: String,
    val district: String?,
    val address: String?,
    val contactPhone: String?,
    val open24h: Boolean = false,
    val ownerAdminId: Long,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val stayOptions: List<StayOption> = emptyList(),
    val roomOptions: List<RoomOption> = emptyList(),
    val placeImageUri: String? = null,
    val licenseImageUri: String? = null
)

