package com.calyrsoft.ucbp1.features.auth.domain.model

@JvmInline
value class Phone private constructor(val value: String) {
    companion object {
        private val REGEX = Regex("^[+]?[0-9()\\-\\s]{6,20}$")

        fun create(raw: String?): Phone {
            val v = raw?.trim() ?: throw IllegalArgumentException("El número de teléfono es requerido")
            require(v.length <= 20) { "Teléfono demasiado largo" }
            require(REGEX.matches(v)) { "Formato de teléfono inválido" }
            return Phone(v)
        }

        fun createOrNull(raw: String?): Phone? =
            try { raw?.takeIf { it.isNotBlank() }?.let { create(it) } } catch (_: Exception) { null }
    }

    override fun toString(): String = value
}