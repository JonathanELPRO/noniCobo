package com.calyrsoft.ucbp1.features.auth.domain.model

data class User(
    val id: Long? = null,
    val username: Username,
    val email: Email,
    val phone: Phone?,
    val role: Role
) {
    companion object {
        /**
         * Crea un `User` desde strings crudos (como hace `LoginUserModel.create(...)`).
         * - Valida y tipa con las value classes.
         * - `phone` puede ser null; si llega en blanco, se ignora.
         * - `role` admite "USER", "ADMIN", "GUEST" (case-insensitive).
         */
        fun create(
            id: Long? = null,
            username: String,
            email: String,
            phone: String?,
            role: String,

        ): User {
            return User(
                id = id,
                username = Username.create(username),
                email = Email.create(email),
                phone = phone?.takeIf { it.isNotBlank() }?.let { Phone.create(it) },
                role = Role.from(role)
            )
        }
    }
}