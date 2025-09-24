package com.calyrsoft.ucbp1.data.repository

import com.calyrsoft.ucbp1.data.datasource.GithubRemoteDataSource
import com.calyrsoft.ucbp1.data.error.DataException
import com.calyrsoft.ucbp1.domain.error.Failure
import com.calyrsoft.ucbp1.domain.model.Nickname
import com.calyrsoft.ucbp1.domain.model.UrlPath
import com.calyrsoft.ucbp1.domain.model.UserModel
import com.calyrsoft.ucbp1.domain.repository.IGithubRepository


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
                return Result.success(UserModel(
                    nickname = Nickname.create(it.login),
                    pathUrl = UrlPath.create(it.url)
                ))
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


