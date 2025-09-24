package com.calyrsoft.ucbp1.features.github.data.repository

import com.calyrsoft.ucbp1.features.dollar.data.error.DataException
import com.calyrsoft.ucbp1.features.dollar.domain.error.Failure
import com.calyrsoft.ucbp1.features.github.domain.model.Nickname
import com.calyrsoft.ucbp1.features.github.domain.model.UrlPath
import com.calyrsoft.ucbp1.features.github.domain.model.UserModel
import com.calyrsoft.ucbp1.features.github.domain.repository.IGithubRepository
import com.calyrsoft.ucbp1.features.github.data.datasource.GithubRemoteDataSource

class GithubRepository(
    val remoteDataSource: GithubRemoteDataSource
): IGithubRepository {
    override suspend fun findByNick(value: String): Result<UserModel> {
        if(value.isEmpty()) {
            return Result.failure(Exception("El campo no puede estar vacio"))
        }
        val response = remoteDataSource.getUser(value)
        //esto de arriba e sun result por eso abajo lo podemos desenvolver con fold
        //este result tiene un githubdto o un error


        response.fold(
            onSuccess = {
                    it ->
                return Result.success(
                    UserModel(
                        nickname = Nickname.Companion.create(it.login),
                        pathUrl = UrlPath.Companion.create(it.url)
                    )
                )
            },

            onFailure = { exception ->
                val failure = when (exception) {
                    is DataException.Network -> Failure.NetworkConnection
                    is DataException.HttpNotFound -> Failure.NotFound
                    is DataException.NoContent -> Failure.EmptyBody
                    is DataException.Unknown -> Failure.Unknown(exception)
                    else -> Failure.Unknown(exception)
                }
                return Result.failure(failure)
            }

        )
    }
}