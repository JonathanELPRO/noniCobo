package com.calyrsoft.ucbp1.features.dollar.domain.usecase

import com.calyrsoft.ucbp1.features.dollar.domain.model.PostModel
import com.calyrsoft.ucbp1.features.dollar.domain.repository.IPostRepository


class GetPostsUseCase(
    private val repository: IPostRepository
) {
    suspend operator fun invoke(): Result<List<PostModel>> {
        return repository.getPosts()
    }
}
