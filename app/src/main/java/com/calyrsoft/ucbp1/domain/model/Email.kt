package com.calyrsoft.ucbp1.domain.model

@JvmInline
value class Email constructor(val value: String) {

    companion object {
        fun create(raw: String): Email {
            val normalized = raw.trim().lowercase()

            require(normalized.isNotEmpty()) {
                "Email must not be empty"
            }
            require(normalized.contains("@")) {
                "Email must contain '@'"
            }

            return Email(value = normalized)
        }
    }

    override fun toString(): String = value
}
