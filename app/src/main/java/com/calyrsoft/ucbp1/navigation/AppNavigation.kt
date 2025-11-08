package com.calyrsoft.ucbp1.navigation

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.calyrsoft.ucbp1.dataStore.AuthDataStore
import com.calyrsoft.ucbp1.features.auth.domain.model.Role
import com.calyrsoft.ucbp1.features.auth.presentation.LoginScreen2
import com.calyrsoft.ucbp1.features.auth.presentation.RegisterScreen
//import com.calyrsoft.ucbp1.features.auth.presentation.LoginScreen
//import com.calyrsoft.ucbp1.features.auth.presentation.RegisterScreen
import com.calyrsoft.ucbp1.features.dollar.presentation.DollarScreen
import com.calyrsoft.ucbp1.features.profile.presentation.ForgotPasswordScreen
import com.calyrsoft.ucbp1.features.github.presentation.GithubScreen
import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import com.calyrsoft.ucbp1.features.lodging.presentation.LodgingDetailsScreen
import com.calyrsoft.ucbp1.features.lodging.presentation.LodgingEditorScreen
import com.calyrsoft.ucbp1.features.lodging.presentation.LodgingListScreen
import com.calyrsoft.ucbp1.features.logout.Logout
import com.calyrsoft.ucbp1.features.movie.presentation.MoviesScreen
import com.calyrsoft.ucbp1.features.posts.presentation.PostsScreen
import com.calyrsoft.ucbp1.features.profile.presentation.ProfileScreen
import com.calyrsoft.ucbp1.features.profile.presentation.SigninPage
import com.calyrsoft.ucbp1.features.reservation.presentation.HistoryScreen
import com.calyrsoft.ucbp1.features.reservation.presentation.PaymentScreen
import com.calyrsoft.ucbp1.features.reservation.presentation.ReservationScreen
import com.google.gson.Gson
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
                                popUpTo(Screen.AuthLogin.route) { inclusive = true }

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
        startDestination = Screen.AuthLogin.route,
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


        // 🔐 AUTH
        composable(Screen.AuthLogin.route) {
            LoginScreen2(
                vm = koinViewModel(),
                onLoginSuccessGoToLodgings = {
                    navigationViewModel.navigateTo(
                        Screen.LodgingList.route,
                        NavigationOptions.CLEAR_BACK_STACK
                    )
                },
                onLoginSuccessGoToRegisterLodging = {
                    navigationViewModel.navigateTo(
                        Screen.LodgingEditor.route,
                        NavigationOptions.CLEAR_BACK_STACK
                    )
                },
                onRegisterClick = {
                    navigationViewModel.navigateTo(
                        Screen.AuthRegister.route,
                        NavigationOptions.DEFAULT
                    )
                }
            )
        }

        composable(Screen.AuthRegister.route) {
            RegisterScreen(
                vm = koinViewModel(),
                onRegisterSuccess = {
                    navigationViewModel.navigateTo(
                        Screen.AuthLogin.route,
                        NavigationOptions.REPLACE_HOME
                    )
                },
                onBackToLogin = {
                    navigationViewModel.navigateTo(
                        Screen.AuthLogin.route,
                        NavigationOptions.REPLACE_HOME
                    )
                }
            )
        }

        // 🏨 LODGING
        composable(Screen.LodgingList.route) {
            LodgingListScreen(
                vm = koinViewModel(),


                onDetails = { id ->
                    navigationViewModel.navigateTo(
                        "lodging_details/$id",
                        NavigationOptions.DEFAULT
                    )
                },
                onEdit = { lodging ->
                    val json = Uri.encode(Gson().toJson(lodging))
                    navigationViewModel.navigateTo(
                        "lodging_edit?lodgingJson=$json",
                        NavigationOptions.DEFAULT
                    )
                }
            )
        }

        composable(
            Screen.LodgingDetails.route,
            arguments = listOf(navArgument("lodgingId") { type = androidx.navigation.NavType.LongType })
        ) { backStack ->
            val id = backStack.arguments!!.getLong("lodgingId")
            LodgingDetailsScreen(
                id = id,
                vm = koinViewModel(),
                onBack = { navController.popBackStack() }
            )
        }


        composable(
            route = Screen.LodgingEditor.route,
            arguments = listOf(navArgument("lodgingJson") {
                type = NavType.StringType
                defaultValue = ""
                nullable = true
            })
        ) { backStackEntry ->
            LodgingEditorScreen(lodgingToEdit = null, currentRole = Role.ADMIN)
        }

        composable(
            route = Screen.LodgingEdit.route,
            arguments = listOf(navArgument("lodgingJson") {
                type = NavType.StringType
                defaultValue = ""
                nullable = true
            })
        ) { backStackEntry ->
            val lodgingJson = backStackEntry.arguments?.getString("lodgingJson")
            val lodgingToEdit = if (!lodgingJson.isNullOrEmpty()) {
                Gson().fromJson(lodgingJson, Lodging::class.java)
            } else null


            LodgingEditorScreen(lodgingToEdit = lodgingToEdit, currentRole = Role.ADMIN)
        }


        // 📅 RESERVATION
        composable(
            Screen.ReservationCreate.route,
            arguments = listOf(
                navArgument("userId") { type = androidx.navigation.NavType.LongType },
                navArgument("lodgingId") { type = androidx.navigation.NavType.LongType }
            )
        ) { backStack ->
            val userId = backStack.arguments!!.getLong("userId")
            val lodgingId = backStack.arguments!!.getLong("lodgingId")
            ReservationScreen(
                vm = koinViewModel(),
                userId = userId,
                lodgingId = lodgingId,
                onCreated = { navController.navigate("reservation_history/$userId") }
            )
        }

        composable(
            Screen.ReservationHistory.route,
            arguments = listOf(navArgument("userId") { type = androidx.navigation.NavType.LongType })
        ) { backStack ->
            val userId = backStack.arguments!!.getLong("userId")
            HistoryScreen(vm = koinViewModel(), userId = userId)
        }

        composable(
            Screen.ReservationPayment.route,
            arguments = listOf(navArgument("reservationId") { type = androidx.navigation.NavType.LongType })
        ) { backStack ->
            val reservationId = backStack.arguments!!.getLong("reservationId")
            PaymentScreen(vm = koinViewModel(), reservationId = reservationId)
        }

        composable(Screen.Logout.route) {
            Logout(
                authViewModel = koinViewModel(),
                onLogoutComplete = {
                    navigationViewModel.navigateTo(
                        Screen.AuthLogin.route,
                        NavigationOptions.CLEAR_BACK_STACK
                    )
                }
            )
        }




    }
}