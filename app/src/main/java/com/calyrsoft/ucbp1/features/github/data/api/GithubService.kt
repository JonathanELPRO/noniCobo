package com.calyrsoft.ucbp1.features.github.data.api

import com.calyrsoft.ucbp1.features.github.data.api.dto.GithubDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubService {
    @GET("/users/{githubLogin}")
    suspend fun getInfoAvatar(@Path("githubLogin") githubLogin: String): Response<GithubDto>
    //en resumen retrofit obtiene un json en formato string, el se da
// cuenta que esta trabajando con gson, entonces ese string en
// formato json lo vuelve un objeto de kotlin, del cual solo
// extrairemos lo que diga el dto
//
//    rawResponse = {
//        code=200,
//        message=OK,
//        headers=[Content-Type: application/json; charset=utf-8, ...],
//        body = "{...json...}"
//    },
//    body = GithubDto(
//    login = "JonathanELPRO",
//    url = "https://avatars.githubusercontent.com/u/106564411?v=4"
//    )
    //algo como lo de arriba tiene el Response<GithubDto> no temos que
    //es una clase generica al mandarle un dto lo estaremos poniendo en su body
}