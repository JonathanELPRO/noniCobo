package com.calyrsoft.ucbp1.domain.repository

import com.calyrsoft.ucbp1.domain.model.CommentModel
import com.calyrsoft.ucbp1.domain.model.MovieModel
import com.calyrsoft.ucbp1.domain.model.PostModel

interface IPostRepository {
    suspend fun getPosts(): Result<List<PostModel>>
    suspend fun getComments(postId: String): Result<List<CommentModel>>
}
