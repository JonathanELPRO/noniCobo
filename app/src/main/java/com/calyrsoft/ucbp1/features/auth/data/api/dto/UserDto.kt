package com.example.imperium_reality.features.login.data.api.dto

import com.google.gson.annotations.SerializedName

data class UserMetadataDto(
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("role") val role: String,
    @SerializedName("username") val username: String,
    @SerializedName("customer_id") val customerId: Int? // nuestro campo entero
)

// Datos del usuario principal
data class UserDto(
    @SerializedName("id") val id: String, // UUID de auth.users
    @SerializedName("email") val email: String,
    @SerializedName("email_confirmed_at") val emailConfirmedAt: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("user_metadata") val userMetadata: UserMetadataDto
)