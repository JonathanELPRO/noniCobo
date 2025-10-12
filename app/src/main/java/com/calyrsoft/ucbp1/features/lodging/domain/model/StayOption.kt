package com.calyrsoft.ucbp1.features.lodging.domain.model

/**
 * Representa una modalidad de estadía (por hora, por noche, por día)
 * con su respectivo precio.
 */
data class StayOption(
    val type: StayType,
)

enum class StayType {
    POR_HORA,
    POR_NOCHE,
    POR_DIA
}
