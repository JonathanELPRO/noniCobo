package com.calyrsoft.ucbp1.features.posts.data.api.dto

data class CommentDto(val postId: String,
                     val id: String,
                     val email: String,
                     val body: String)