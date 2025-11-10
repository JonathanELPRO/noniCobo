package com.example.ucbp1.features.register.data.api.dto

import com.google.gson.annotations.SerializedName

data class UpdateUserDto (
    @SerializedName("username") val username: String,
    @SerializedName("phone") val phone: String,
)