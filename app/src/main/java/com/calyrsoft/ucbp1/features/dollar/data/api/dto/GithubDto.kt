package com.calyrsoft.ucbp1.features.dollar.data.api.dto
import com.google.gson.annotations.SerializedName

data class GithubDto(val login: String,
                     @SerializedName("avatar_url") val url: String)

//@SerializedName es una anotación que indica que el campo JSON "avatar_url" debe mapearse a la propiedad url de Kotlin.
/*
{
    "login": "JonathanELPRO",
    "id": 106564411,
    "node_id": "U_kgDOBloLOw",
    "avatar_url": "https://avatars.githubusercontent.com/u/106564411?v=4"
}*/
