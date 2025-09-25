package com.calyrsoft.ucbp1.features.profile.domain.model

@JvmInline
value class Phone private constructor(val value: String) {
    companion object {
        fun create(raw: String): Phone {
            val normalized = raw.filter { it.isDigit() }
            require(normalized.length in 7..15) { "Phone must have between 7 and 15 digits" }
            return Phone(value = normalized)
        }
    }
    override fun toString(): String = value
}
