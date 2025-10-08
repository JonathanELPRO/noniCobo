package com.calyrsoft.ucbp1.features.auth.domain.model

data class User(
    val id: Long? = null,
    val username: String,
    val email: String,
    val phone: String?,
    val role: Role
)