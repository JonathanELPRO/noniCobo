package com.calyrsoft.ucbp1.features.posts.data.datasource

import com.calyrsoft.ucbp1.features.dollar.data.error.DataException
import com.calyrsoft.ucbp1.features.posts.data.api.PostsService
import com.calyrsoft.ucbp1.features.posts.data.api.dto.CommentDto
import com.calyrsoft.ucbp1.features.posts.data.api.dto.PostDto

class PostsRemoteDataSource(
    private val postsService: PostsService
) {
    suspend fun getPosts(): Result<List<PostDto>> {
        val response = postsService.getPosts()

        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return Result.success(body)
            } else {
                return Result.failure(DataException.NoContent)
            }
        } else {
            if (response.code() == 404) {
                return Result.failure(DataException.HttpNotFound)
            } else {
                return Result.failure(
                    DataException.Unknown("HTTP ${response.code()} ${response.message()}")
                )
            }
        }
    }

    suspend fun getComments(postId: String): Result<List<CommentDto>> {
        val response = postsService.getComments(postId)

        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return Result.success(body)
            } else {
                return Result.failure(DataException.NoContent)
            }
        } else {
            if (response.code() == 404) {
                return Result.failure(DataException.HttpNotFound)
            } else {
                return Result.failure(
                    DataException.Unknown("HTTP ${response.code()} ${response.message()}")
                )
            }
        }
    }



    }