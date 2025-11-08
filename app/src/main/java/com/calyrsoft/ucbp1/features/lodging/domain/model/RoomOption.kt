package com.calyrsoft.ucbp1.features.lodging.domain.model

import kotlinx.serialization.Serializable

/**
 * Representa un tipo de habitación (simple, con baño, con baño y TV)
 * con su respectivo precio.
 */
@Serializable
data class RoomOption(
    val category: RoomCategory,
    val price: Double
)

@Serializable
enum class RoomCategory {
    SIMPLE,
    CON_BAÑO,
    CON_BAÑO_Y_TV
}
