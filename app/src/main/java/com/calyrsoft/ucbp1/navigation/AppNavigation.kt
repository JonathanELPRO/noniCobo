package com.calyrsoft.ucbp1.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.calyrsoft.ucbp1.features.auth.presentation.LoginScreen2
import com.calyrsoft.ucbp1.features.auth.presentation.RegisterScreen
import com.calyrsoft.ucbp1.features.profile.presentation.ForgotPasswordScreen
import com.calyrsoft.ucbp1.features.github.presentation.GithubScreen
import com.calyrsoft.ucbp1.features.lodging.presentation.LodgingDetailsScreen
import com.calyrsoft.ucbp1.features.lodging.presentation.LodgingEditorScreen
import com.calyrsoft.ucbp1.features.lodging.presentation.LodgingListScreen
import com.calyrsoft.ucbp1.features.logout.Logout
import com.calyrsoft.ucbp1.features.payments.domain.model.PaymentModel
import com.calyrsoft.ucbp1.features.payments.presentation.PaymentScreen
import com.calyrsoft.ucbp1.features.privacy.presentation.PrivacyScreen
import com.calyrsoft.ucbp1.features.profile.presentation.ProfileScreen
import com.calyrsoft.ucbp1.features.profile.presentation.SigninPage
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.koinViewModel
import java.net.URLDecoder
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
                },
                goToPrivacy = {
                    navigationViewModel.navigateTo(
                        Screen.PrivacyScreen.route,
                        NavigationOptions.DEFAULT
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
                onBack = { navController.popBackStack() },
                onPagos = { payment ->
                    val paymentJson = Json.encodeToString(payment)
                    val encodedPayment = URLEncoder.encode(paymentJson, "UTF-8")
                    navController.navigate("${Screen.PaymentScreen.route}/${encodedPayment}")
                }
            )
        }

        composable(
            route = "${Screen.PaymentScreen.route}/{payment}",
            arguments = listOf(
                navArgument("payment") { type = androidx.navigation.NavType.StringType }
            )
        ) { backStack ->
            val encodedPayment = backStack.arguments?.getString("payment") ?: ""

            val decodedJson = URLDecoder.decode(encodedPayment, "UTF-8")

            val payment = Json.decodeFromString<PaymentModel>(decodedJson)

            PaymentScreen(
                model = payment,
                onBack = { navController.popBackStack() }
            )
        }


        composable(
            Screen.LodgingEditor.route
        ) {
            LodgingEditorScreen(
                currentRole = com.calyrsoft.ucbp1.features.auth.domain.model.Role.ADMIN,
                vm = koinViewModel(),
                onSaved = { navController.popBackStack() }
            )
        }

        // 📅 RESERVATION





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

        composable(Screen.PrivacyScreen.route) {
            PrivacyScreen(
                goBack = {
                    navigationViewModel.popBackStack(
                    )
                }
            )
        }





    }
}