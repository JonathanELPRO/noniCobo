package com.calyrsoft.ucbp1.navigation

sealed class Screen(val route: String) {
    object LoginScreen : Screen("login")
    object GithubScreen : Screen("github")



    object ProfileScreen : Screen("profile_screen/{name}")



    object Dollar: Screen("dollar")
    object ForgotPasswordScreen : Screen("forgot_password")
    object MoviesScreen : Screen("movies")
    object PostsScreen : Screen("posts")



}