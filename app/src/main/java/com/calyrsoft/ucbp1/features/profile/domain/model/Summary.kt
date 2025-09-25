package com.calyrsoft.ucbp1.features.profile.domain.model

@JvmInline
value class Summary private constructor(val value: String) {
    companion object {
        fun create(raw: String): Summary {
            val normalized = raw.trim()
            require(normalized.isNotEmpty()) { "Summary must not be empty" }
            require(normalized.length <= 500) { "Summary must not exceed 500 characters" }
            return Summary(value = normalized)
        }
    }
    override fun toString(): String = value
}
