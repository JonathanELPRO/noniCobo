package com.example.ucbp1.features.register.data.api.dto

import com.google.gson.annotations.SerializedName

data class RegisterRequestDto(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("data") val data: RegisterUserDto
)
