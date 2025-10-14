package com.example.ucbp1.features.register.data.api.dto

import com.google.gson.annotations.SerializedName

data class RegisterResponseDto(
    @SerializedName("email") val email: String
)
