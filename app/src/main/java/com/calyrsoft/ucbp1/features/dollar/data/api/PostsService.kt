package com.calyrsoft.ucbp1.features.dollar.data.api

import com.calyrsoft.ucbp1.features.dollar.data.api.dto.CommentDto
import com.calyrsoft.ucbp1.features.dollar.data.api.dto.PostDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PostsService {
    @GET("/posts")
    suspend fun getPosts(): Response<List<PostDto>>

    @GET("/comments")
    suspend fun getComments(
        @Query("postId") postId: String = "1",
    ): Response<List<CommentDto>>


}