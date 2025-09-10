package com.calyrsoft.ucbp1.data.datasource
import com.calyrsoft.ucbp1.data.api.GithubService
import com.calyrsoft.ucbp1.data.api.dto.GithubDto


class GithubRemoteDataSource(
    val githubService: GithubService
) {
    suspend fun getUser(nick: String): Result<GithubDto> {
        val response = githubService.getInfoAvatar(nick)
        if (response.isSuccessful) {
            return Result.success(response.body()!!)
        } else {
            return Result.failure(Exception("Error al obtener el usuario"))
        }
    }
}
