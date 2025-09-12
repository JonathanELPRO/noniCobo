package com.calyrsoft.ucbp1.domain.usecase

import com.calyrsoft.ucbp1.domain.model.MovieModel
import com.calyrsoft.ucbp1.domain.model.PostModel
import com.calyrsoft.ucbp1.domain.repository.IMoviesRepository
import com.calyrsoft.ucbp1.domain.repository.IPostRepository


class GetPostsUseCase(
    private val repository: IPostRepository
) {
    suspend operator fun invoke(): Result<List<PostModel>> {
        return repository.getPosts()
    }
}
