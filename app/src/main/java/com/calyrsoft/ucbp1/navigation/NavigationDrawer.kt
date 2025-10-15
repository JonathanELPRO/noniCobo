package com.calyrsoft.ucbp1.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Hotel
import androidx.compose.material.icons.outlined.Login
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector



sealed class NavigationDrawer(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String) {


//    data object Profile : NavigationDrawer("Profile", Icons.Filled.Home,
//        Icons.Outlined.Home, Screen.LoginScreen.route
//    )
//    data object Dollar : NavigationDrawer("Dollar",
//        Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart,
//        Screen.Dollar.route)
//    data object Github : NavigationDrawer("Github",
//        Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder,
//        Screen.GithubScreen.route)
//    data object Movie : NavigationDrawer("Movie",
//        Icons.Filled.DateRange, Icons.Outlined.DateRange,
//        Screen.MoviesScreen.route)

    // ======================================================
    // 🔐 AUTH
    // ======================================================
    data object AuthLogin : NavigationDrawer(
        "Iniciar Sesión",
        Icons.Filled.Login,
        Icons.Outlined.Login,
        Screen.AuthLogin.route
    )

    data object AuthRegister : NavigationDrawer(
        "Registrarse",
        Icons.Filled.AccountBox,
        Icons.Outlined.AccountBox,
        Screen.AuthRegister.route
    )

    // ======================================================
    // 🏨 LODGING
    // ======================================================
    data object LodgingList : NavigationDrawer(
        "Alojamientos",
        Icons.Filled.Hotel,
        Icons.Outlined.Hotel,
        Screen.LodgingList.route
    )

    data object LodgingEditor : NavigationDrawer(
        "Nuevo Alojamiento",
        Icons.Filled.Create,
        Icons.Outlined.Create,
        Screen.LodgingEditor.route
    )

    // ======================================================
    // 📅 RESERVATION
    // ======================================================
    data object ReservationCreate : NavigationDrawer(
        "Crear Reserva",
        Icons.Filled.DateRange,
        Icons.Outlined.DateRange,
        Screen.ReservationCreate.route
    )

    data object ReservationHistory : NavigationDrawer(
        "Historial de Reservas",
        Icons.Filled.History,
        Icons.Outlined.History,
        Screen.ReservationHistory.route
    )

    data object ReservationPayment : NavigationDrawer(
        "Pagos",
        Icons.Filled.Payments,
        Icons.Outlined.Payments,
        Screen.ReservationPayment.route
    )

    data object Logout : NavigationDrawer(
        "Cerrar Sesion",
        Icons.Filled.Logout,
        Icons.Outlined.Logout,
        Screen.Logout.route
    )
}
