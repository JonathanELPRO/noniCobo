package com.calyrsoft.ucbp1.features.github.data.datasource

import com.calyrsoft.ucbp1.features.dollar.data.error.DataException
import com.calyrsoft.ucbp1.features.github.data.api.GithubService
import com.calyrsoft.ucbp1.features.github.data.api.dto.GithubDto

class GithubRemoteDataSource(
    private val githubService: GithubService
) {
    suspend fun getUser(nick: String): Result<GithubDto> {
        val response = githubService.getInfoAvatar(nick)

        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return Result.success(body)
            } else {
                return Result.failure(DataException.NoContent)
            }
        } else {
            if (response.code() == 404) {
                return Result.failure(DataException.HttpNotFound)
            } else {
                return Result.failure(
                    DataException.Unknown("HTTP ${response.code()} ${response.message()}")
                )
            }
        }
    }
}