package com.example.imperium_reality.features.login.data.api.dto

import com.google.gson.annotations.SerializedName

data class LoginResponseDto(
    @SerializedName("user") val user: UserDto,
    @SerializedName("access_token") val token: String
)
