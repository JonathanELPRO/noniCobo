package com.calyrsoft.ucbp1.features.auth.domain.model

@JvmInline
value class Username private constructor(val value: String) {
    companion object {
        private val REGEX = Regex("^[A-Za-z0-9._-]{4,30}$")

        fun create(raw: String?): Username {
            val v = raw?.trim() ?: throw IllegalArgumentException("El nombre de usuario es requerido")
            require(REGEX.matches(v)) { "Nombre de usuario inválido (4–30 caracteres, solo letras, números,sin espacios)" }
            return Username(v)
        }
    }

    override fun toString(): String = value
}