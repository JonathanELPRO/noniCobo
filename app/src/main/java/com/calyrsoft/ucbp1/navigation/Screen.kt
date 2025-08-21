package com.calyrsoft.ucbp1.navigation

sealed class Screen(val route: String) {
    object LoginScreen : Screen("login")
    object GithubScreen : Screen("github")
}