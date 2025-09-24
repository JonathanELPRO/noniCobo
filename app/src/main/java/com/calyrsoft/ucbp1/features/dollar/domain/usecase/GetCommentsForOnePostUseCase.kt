package com.calyrsoft.ucbp1.features.dollar.domain.usecase

import com.calyrsoft.ucbp1.features.dollar.domain.model.CommentModel
import com.calyrsoft.ucbp1.features.dollar.domain.repository.IPostRepository


class GetCommentsForOnePostUseCase(
    private val repository: IPostRepository
) {
    suspend operator fun invoke(postId: String): Result<List<CommentModel>> {
        return repository.getComments(postId)
    }
}