package com.calyrsoft.ucbp1.features.dollar.domain.model

@JvmInline
value class Nickname constructor(val value: String) {

    companion object {
        fun create(raw: String): Nickname {
            val normalized = raw.trim()

            require(normalized.isNotEmpty()) { "Nickname must not be empty" }
            require(normalized.length in 3..20) { "Nickname length must be between 3 and 20 characters" }
            require(normalized.all { it.isLetterOrDigit() || it == '_' }) {
                "Nickname must contain only letters, digits or '_'"
            }

            return Nickname(normalized)
        }


    }

    override fun toString(): String = value
    //el override de arriba se hace para:
//    val nick = Nickname.create("juan")
//    println(nick)
//// Podría mostrar: Nickname(value=juan)
}
