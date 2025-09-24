package com.calyrsoft.ucbp1.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.calyrsoft.ucbp1.presentation.DollarScreen
import com.calyrsoft.ucbp1.presentation.ForgotPasswordScreen
import com.calyrsoft.ucbp1.presentation.GithubScreen
import com.calyrsoft.ucbp1.presentation.MoviesScreen
import com.calyrsoft.ucbp1.presentation.PostsScreen
import com.calyrsoft.ucbp1.presentation.ProfileScreen
import com.calyrsoft.ucbp1.presentation.SigninPage
import org.koin.androidx.compose.koinViewModel
import java.net.URLEncoder
import androidx.navigation.compose.NavHost


@Composable
fun AppNavigation(modifier: Modifier, navController: NavHostController, startDestination: String) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
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
                },
                navToForgotPassword = {
                    navController.navigate(Screen.ForgotPasswordScreen.route)
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
            Screen.ProfileScreen.route,
            arguments = listOf(
                navArgument("name") { defaultValue = "" }
                //,navArgument("age") { defaultValue = 0 },
                )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""


            ProfileScreen(
                modifier = modifier,
                name = name,
                vm = koinViewModel(),

                onEndSession = {
                    navController.navigate(
                        Screen.LoginScreen.route
                    )
                },

                onAskExchangeRate = {
                    navController.navigate(
                        Screen.Dollar.route
                    )
                }
            )
        }

        //TODO HASTA ONEND SESSION Y ONASKEXCHANGE RATE YA LO ESTUDIASTE

//        composable(Screen.ExchangeRateScreen.route) {
//            ExchangeRateScreen(
//                modifier = modifier,
//                vm = koinViewModel()
//            )
//        }


        composable(Screen.Dollar.route) {
            DollarScreen(viewModelDollar = koinViewModel())
        }


        composable(Screen.ForgotPasswordScreen.route) {
            ForgotPasswordScreen(
                modifier = modifier,
                vm = koinViewModel(),
                onBackToLogin = {
                    navController.navigate(Screen.LoginScreen.route) {
                        popUpTo(Screen.LoginScreen.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.MoviesScreen.route) {
            MoviesScreen(
                modifier = modifier,
                vm = koinViewModel()
            )
        }

        composable(Screen.PostsScreen.route) {
            PostsScreen(
                modifier = modifier,
                vm = koinViewModel()
            )
        }



    }
}