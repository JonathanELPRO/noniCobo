package com.calyrsoft.ucbp1.features.auth.domain.model

@JvmInline
value class Email private constructor(val value: String) {
    companion object {
        private val REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

        fun create(raw: String?): Email {
            val v = raw?.trim()?.lowercase() ?: throw IllegalArgumentException("El correo electrónico es requerido")
            require(v.length <= 254) { "Correo demasiado largo" }
            require(REGEX.matches(v)) { "Formato de correo inválido" }
            return Email(v)
        }
    }

    override fun toString(): String = value
}