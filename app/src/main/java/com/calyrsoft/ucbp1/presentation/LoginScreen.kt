package com.calyrsoft.ucbp1.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.calyrsoft.ucbp1.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun SigninPage(modifier: Modifier,
               vm : LoginViewModel = koinViewModel(),
               onSuccess: (name: String) -> Unit,
               navToForgotPassword: () -> Unit) {
    //arriba estamos definiendo un callback
    var userSignIn by remember { mutableStateOf("") }
    var passwordSignIn by remember { mutableStateOf("") }
    val state by vm.state.collectAsState()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Sign In")

        TextField(
            label = {
                Text(text = "User")
            },
            singleLine = true,
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                ),
            placeholder = {
                Text(text = "placeholder")
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_launcher_background),
                    contentDescription = "",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp),
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors =
                TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Green,
                    unfocusedIndicatorColor = Color.Yellow,
                ),
            value = userSignIn, onValueChange = { userSignIn = it },
        )

        TextField(value = passwordSignIn, onValueChange = { passwordSignIn = it })

        Button(modifier = Modifier.fillMaxWidth(),
            onClick = { vm.fetchAlias(userSignIn, passwordSignIn) })
        {
            Text(text = stringResource(R.string.signin_button))
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { navToForgotPassword() } // callback que recibe SigninPage
        ) {
            Text("Olvidé mi contraseña")
        }

        val context = LocalContext.current

        Button(
            onClick = {
                val number = vm.openWhatsapp()
                val url = "https://wa.me/$number"

                // Crear el intent de forma concisa usando toUri()
                val intent = android.content.Intent(
                    android.content.Intent.ACTION_VIEW,
                    url.toUri()
                )

                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Contactar por WhatsApp")
        }

        when(val st = state){
            is LoginViewModel.LoginStateUI.Error -> {
                Text(st.message)
            }
            LoginViewModel.LoginStateUI.Init -> {
                Text("Init")
            }
            LoginViewModel.LoginStateUI.Loading -> {
                Text("Loading")
            }
            is LoginViewModel.LoginStateUI.Success -> {
                Text("Se logueo correctamente al usuario: " + st.loginUser.name)
                onSuccess(
                    st.loginUser.name
                )

            }
        }


    }
//    Column(
//        modifier = modifier,
//    ) {
//        Text(
//            text = "Hello $name!",
//        )
//
//        Button(onClick = {
//            Sentry.captureException(
//                RuntimeException("THIS APP USES SENTRY"),
//            )
//        }) {
//            Text(text = "Capture Exception")
//        }
//    }
}



