package com.calyrsoft.ucbp1.features.dollar.data.repository

import com.calyrsoft.ucbp1.features.dollar.data.datasource.PostsRemoteDataSource
import com.calyrsoft.ucbp1.features.dollar.domain.model.CommentModel
import com.calyrsoft.ucbp1.features.dollar.domain.model.PostModel
import com.calyrsoft.ucbp1.features.dollar.domain.repository.IPostRepository

class PostRepository(
    private val remote: PostsRemoteDataSource
) : IPostRepository {

    override suspend fun getPosts(): Result<List<PostModel>> {
        val response = remote.getPosts()
        response.fold(
            onSuccess = { page ->
                var dtos = page
                var models = dtos.map { dto ->
                    PostModel(
                        userId = dto.userId,
                        id = dto.id,
                        title = dto.title,
                        body = dto.body,
                    )
                }
                return Result.success(models)
            },
            onFailure = { exception -> return Result.failure(exception) }
        )
    }

    override suspend fun getComments(postId: String): Result<List<CommentModel>> {
        val response = remote.getComments(postId)
        response.fold(
            onSuccess = { page ->
                var dtos = page
                var models = dtos.map { dto ->
                    CommentModel(
                        postId = dto.postId,
                        id = dto.id,
                        email = dto.email,
                        body = dto.body,
                    )
                }
                return Result.success(models)
            },
            onFailure = { exception -> return Result.failure(exception) }
        )
    }


}