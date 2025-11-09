package com.calyrsoft.ucbp1.features.profile.data.api.dto

import com.google.gson.annotations.SerializedName

data class UpdateUserPasswordDto (
    @SerializedName("password") val password: String
)