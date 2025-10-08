package com.calyrsoft.ucbp1.features.lodging.domain.model

/**
 * Representa un tipo de habitación (simple, con baño, con baño y TV)
 * con su respectivo precio.
 */
data class RoomOption(
    val category: RoomCategory,
    val price: Double
)

enum class RoomCategory {
    SIMPLE,
    CON_BAÑO,
    CON_BAÑO_Y_TV
}
