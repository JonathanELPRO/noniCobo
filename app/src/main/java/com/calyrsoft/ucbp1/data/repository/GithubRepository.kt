package com.calyrsoft.ucbp1.data.repository

import com.calyrsoft.ucbp1.domain.model.UserModel
import com.calyrsoft.ucbp1.domain.repository.IGithubRepository

class GithubRepository : IGithubRepository {
    override fun findByNick(value: String): Result<UserModel> {
        return Result.success(UserModel("calyr", "https://avatars.githubusercontent.com/u/874321?v=4"))
    }
}
