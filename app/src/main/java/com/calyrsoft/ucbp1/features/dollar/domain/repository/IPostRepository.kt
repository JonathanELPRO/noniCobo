package com.calyrsoft.ucbp1.features.dollar.domain.repository

import com.calyrsoft.ucbp1.features.dollar.domain.model.CommentModel
import com.calyrsoft.ucbp1.features.dollar.domain.model.PostModel

interface IPostRepository {
    suspend fun getPosts(): Result<List<PostModel>>
    suspend fun getComments(postId: String): Result<List<CommentModel>>
}
