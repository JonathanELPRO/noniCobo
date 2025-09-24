package com.calyrsoft.ucbp1.features.posts.domain.usecase

import com.calyrsoft.ucbp1.features.posts.domain.model.CommentModel
import com.calyrsoft.ucbp1.features.posts.domain.repository.IPostRepository


class GetCommentsForOnePostUseCase(
    private val repository: IPostRepository
) {
    suspend operator fun invoke(postId: String): Result<List<CommentModel>> {
        return repository.getComments(postId)
    }
}