package com.calyrsoft.ucbp1.features.auth.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.calyrsoft.ucbp1.R
import com.calyrsoft.ucbp1.dataStore.AuthDataStore
import com.calyrsoft.ucbp1.features.auth.domain.model.Role
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen2(
    vm: LoginViewModel2,
    onLoginSuccessGoToLodgings: () -> Unit = {},
    onLoginSuccessGoToRegisterLodging: (Long?) -> Unit = {},
    onRegisterClick: () -> Unit = {},
) {
    LaunchedEffect(Unit) {
        vm.getToken()
    }

    val state by vm.state.collectAsState()
    var userOrEmail by remember { mutableStateOf("") }
    var idUsuario by remember { mutableStateOf(0L) }
    var password by remember { mutableStateOf("") }
    val authViewModel: AuthViewModel = getViewModel()

    val userRole by authViewModel.userRole.collectAsState(initial = null)



    Log.d("LoginScreen2", "userRole: $userRole")

    // Si ya hay un rol guardado (usuario logeado previamente)
    LaunchedEffect(userRole) {
        if (!userRole.isNullOrEmpty()) { // evita null
            when (userRole) {
                "CLIENT" -> onLoginSuccessGoToLodgings()
                "ADMIN" -> onLoginSuccessGoToRegisterLodging(idUsuario)
            }
            vm.resetState() // limpiar state inmediatamente
        }
    }

    Scaffold(containerColor = Color(0xFFF4F4F4)) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(bottomStart = 80.dp, bottomEnd = 80.dp))
                    .background(Color(0xFFB00020)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Imagen de cabecera",
                    modifier = Modifier.size(180.dp).clip(RoundedCornerShape(90.dp))
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Login", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = userOrEmail,
                onValueChange = { userOrEmail = it },
                label = { Text("Usuario o correo") },
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (val st = state) {
                is LoginViewModel2.LoginUIState.Loading -> CircularProgressIndicator(color = Color(0xFFB00020))
                is LoginViewModel2.LoginUIState.Error -> Text(st.message, color = MaterialTheme.colorScheme.error)
                is LoginViewModel2.LoginUIState.Success -> {
                    if (!userRole.isNullOrEmpty()) { // evita null
                        when (userRole) {
                            "CLIENT" -> onLoginSuccessGoToLodgings()
                            "ADMIN" -> onLoginSuccessGoToRegisterLodging(st.user.id)
                        }
                        idUsuario=st.user.id?:0L
                        vm.resetState() // limpiar state inmediatamente
                    }
                }
                else -> Unit
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { vm.login(userOrEmail, password) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020)),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp).height(50.dp),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Iniciar sesión", color = Color.White, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onRegisterClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBDB304)),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp).height(50.dp),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Crear cuenta", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
