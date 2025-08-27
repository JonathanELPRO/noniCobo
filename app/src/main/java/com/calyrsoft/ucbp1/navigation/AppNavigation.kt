package com.calyrsoft.ucbp1.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.calyrsoft.ucbp1.presentation.GithubScreen
import com.calyrsoft.ucbp1.presentation.ProfileScreen
import com.calyrsoft.ucbp1.presentation.SigninPage
import org.koin.androidx.compose.koinViewModel
import java.net.URLEncoder

@Composable
fun AppNavigation(modifier: Modifier){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.LoginScreen.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(Screen.LoginScreen.route) {
            SigninPage(
                modifier = modifier,
                vm = koinViewModel(),
                onSuccess = { name ->
                    val encodedName = URLEncoder.encode(name, "UTF-8")

                    navController.navigate(
                        "profile_screen/$encodedName"
                    )
                }
            )
        }

        //En pocas palabras: la ruta (Screen.LoginScreen)
        // y la pantalla (SigninPage) están conectadas en el NavHost mediante el bloque composable.

        composable(Screen.GithubScreen.route) {
            GithubScreen(
                modifier = modifier,
                vm = koinViewModel()
            )
        }

        composable(
            route = "profile_screen/{name}",
            arguments = listOf(
                navArgument("name") { defaultValue = "" }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""


            ProfileScreen(
                modifier = modifier,
                name = name,


            )
        }



    }
}