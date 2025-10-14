package com.example.ucbp1.features.register.data.api.dto

import com.google.gson.annotations.SerializedName

data class RegisterUserDto (
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("role") val role: String,
    @SerializedName("phone") val phone: String,
)