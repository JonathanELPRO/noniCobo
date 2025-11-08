package com.calyrsoft.ucbp1.features.lodging.domain.model

import kotlinx.serialization.Serializable

/**
 * Representa una modalidad de estadía (por hora, por noche, por día)
 * con su respectivo precio.
 */
@Serializable
data class StayOption(
    val type: StayType,
)

@Serializable
enum class StayType {
    POR_HORA,
    POR_NOCHE,
    POR_DIA
}
