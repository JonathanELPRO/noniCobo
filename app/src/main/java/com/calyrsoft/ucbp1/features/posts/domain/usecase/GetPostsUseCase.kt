package com.calyrsoft.ucbp1.features.posts.domain.usecase

import com.calyrsoft.ucbp1.features.posts.domain.model.PostModel
import com.calyrsoft.ucbp1.features.posts.domain.repository.IPostRepository


class GetPostsUseCase(
    private val repository: IPostRepository
) {
    suspend operator fun invoke(): Result<List<PostModel>> {
        return repository.getPosts()
    }
}
