package com.example.imperium_reality.features.register.data.api.dto

import com.google.gson.annotations.SerializedName

data class RegisterResponseDto(
    @SerializedName("email") val email: String
)
