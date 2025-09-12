package com.calyrsoft.ucbp1.data.api.dto

import com.google.gson.annotations.SerializedName

data class PostDto(val userId: String,
                   val id: String,
                   val title: String,
                   val body: String)

