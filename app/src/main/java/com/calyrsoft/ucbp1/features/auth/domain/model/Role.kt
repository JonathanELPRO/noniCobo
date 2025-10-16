package com.calyrsoft.ucbp1.features.auth.domain.model


enum class Role {
    CLIENT,
    ADMIN,
    GUEST;

    companion object {
        fun from(raw: String?): Role = when (raw?.trim()?.uppercase()) {
            "ADMIN" -> ADMIN
            "CLIENT" -> CLIENT
            else -> CLIENT
        }
    }
}