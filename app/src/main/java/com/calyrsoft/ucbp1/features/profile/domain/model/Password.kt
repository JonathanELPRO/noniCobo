package com.calyrsoft.ucbp1.features.profile.domain.model

@JvmInline
value class Password private constructor(val value: String) {
    companion object {
        fun create(raw: String): Password {
            val normalized = raw.trim().lowercase()
            require(raw.length >= 6) { "Password must be at least 6 characters long" }
            return Password(value = normalized)
        }
    }
    override fun toString(): String = "******"
}
