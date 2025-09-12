package com.calyrsoft.ucbp1.navigation

sealed class Screen(val route: String) {
    object LoginScreen : Screen("login")
    object GithubScreen : Screen("github")

    val ProfileScreen = object {
        val route = "profile_screen/{name}"
    }

    object ExchangeRateScreen : Screen("exchangeRate")
    object ForgotPasswordScreen : Screen("forgot_password")
    object MoviesScreen : Screen("movies")
    object PostsScreen : Screen("posts")



}