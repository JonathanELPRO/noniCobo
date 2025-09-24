package com.calyrsoft.ucbp1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.calyrsoft.ucbp1.navigation.AppNavigation
import com.calyrsoft.ucbp1.navigation.Screen
import com.calyrsoft.ucbp1.ui.theme.Ucbp1Theme

class MainActivity : ComponentActivity() {

    @SuppressLint("ContextCastToActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Ucbp1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    val context = LocalContext.current as MainActivity

                    // 👉 siempre arranca en Dollar
                    AppNavigation(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = Screen.GithubScreen.route
                    )

                    // 👉 Escucha el intent actual y navega si trae "route"
                    LaunchedEffect(context.intent) {
                        val route = context.intent.getStringExtra("route")
                        if (route != null && route != Screen.GithubScreen.route) {
                            navController.navigate(route) {
                                popUpTo(0) // limpia el backstack si quieres
                            }
                        }
                    }
                }
            }
        }
    }

    // 👉 cuando la app ya está abierta y llega una notificación
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent) // muy importante: actualiza el intent para que Compose lo detecte
    }

    //onNewIntent + setIntent es el puente que permite que tu app ya abierta pueda reaccionar a un nuevo
    //es decir esas dos lineas hacen que si se genera un nuevo intent con la notificacion pues entremos a ese
}
