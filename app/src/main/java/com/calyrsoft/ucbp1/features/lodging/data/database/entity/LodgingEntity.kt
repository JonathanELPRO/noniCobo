package com.calyrsoft.ucbp1.features.lodging.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.calyrsoft.ucbp1.features.lodging.domain.model.RoomOption
import com.calyrsoft.ucbp1.features.lodging.domain.model.StayOption

@Entity(tableName = "lodgings")
@TypeConverters(Converters::class)
data class LodgingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
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

    // 🔹 aquí se guarda el contenido binario, no texto
    val placeImage: ByteArray? = null,
    val licenseImage: ByteArray? = null
)
