package com.calyrsoft.ucbp1.features.profile.domain.model

@JvmInline
value class Name private constructor(val value: String) {
    companion object {
        fun create(raw: String): Name {
            val normalized = raw.trim().lowercase()
            require(normalized.isNotEmpty()) { "Name must not be empty" }
            require(normalized.length >= 2) { "Name must have at least 2 characters" }
            return Name(value = normalized)
        }
    }
    override fun toString(): String = value
}
