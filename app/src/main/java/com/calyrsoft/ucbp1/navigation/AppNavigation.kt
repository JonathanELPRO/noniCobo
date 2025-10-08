package com.calyrsoft.ucbp1.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.calyrsoft.ucbp1.features.dollar.presentation.DollarScreen
import com.calyrsoft.ucbp1.features.profile.presentation.ForgotPasswordScreen
import com.calyrsoft.ucbp1.features.github.presentation.GithubScreen
import com.calyrsoft.ucbp1.features.movie.presentation.MoviesScreen
import com.calyrsoft.ucbp1.features.posts.presentation.PostsScreen
import com.calyrsoft.ucbp1.features.profile.presentation.ProfileScreen
import com.calyrsoft.ucbp1.features.profile.presentation.SigninPage
import org.koin.androidx.compose.koinViewModel
import java.net.URLEncoder


@Composable
fun AppNavigation(navigationViewModel: NavigationViewModel, modifier: Modifier, navController: NavHostController) {

    LaunchedEffect(navigationViewModel) {   // clave asociada al VM
        navigationViewModel.navigationCommands.collect { command ->
            when (command) {
                is NavigationViewModel.NavigationCommand.NavigateTo -> {
                    Log.d("NavHost", "🧭 route=${command.route} opt=${command.options}")
                    navController.navigate(command.route) {
                        when (command.options) {
                            NavigationOptions.CLEAR_BACK_STACK -> {
                                popUpTo(0)
                            }
                            //lo de arriba borra absolutamente toda la pila de visitas
                            NavigationOptions.REPLACE_HOME -> {
                                popUpTo(Screen.LoginScreen.route) { inclusive = true }

                            }
                            //lo de arriba solo borra la pila de visitas hasta llegar al loginscreen mas reciene lo borra igual ese pero
                            else -> { /* normal */ }
                        }
                    }
                }
                NavigationViewModel.NavigationCommand.PopBackStack -> {
                    Log.d("NavHost", "⬅️ PopBackStack")
                    navController.popBackStack()
                }
            }
        }
    }


    NavHost(
        navController = navController,
        startDestination = Screen.LoginScreen.route,
        //no impirta que valor pongas arriba no empezaremos ahi por culpa del else de handleDeepLink
        //que se llama en el primer launched effect de main activity
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
                },
                onMovies = {
                    navController.navigate(Screen.MoviesScreen.route)
                },
                onDollar = {
                    navController.navigate(Screen.Dollar.route)
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