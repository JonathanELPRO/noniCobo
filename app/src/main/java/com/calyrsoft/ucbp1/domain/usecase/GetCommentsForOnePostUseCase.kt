package com.calyrsoft.ucbp1.domain.usecase

import com.calyrsoft.ucbp1.domain.model.CommentModel
import com.calyrsoft.ucbp1.domain.model.PostModel
import com.calyrsoft.ucbp1.domain.repository.IPostRepository


class GetCommentsForOnePostUseCase(
    private val repository: IPostRepository
) {
    suspend operator fun invoke(postId: String): Result<List<CommentModel>> {
        return repository.getComments(postId)
    }
}