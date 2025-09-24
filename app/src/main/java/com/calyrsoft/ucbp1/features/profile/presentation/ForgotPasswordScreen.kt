package com.calyrsoft.ucbp1.features.profile.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.calyrsoft.ucbp1.features.dollar.presentation.WhatsappButton
import org.koin.androidx.compose.koinViewModel

@Composable
fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    vm: ForgotPasswordViewModel = koinViewModel(),
    onBackToLogin: () -> Unit
) {
    val state by vm.state.collectAsState()


    var username by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Olvidé mi contraseña", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(24.dp))

        // Campo de usuario
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Nombre de usuario") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { vm.checkUserExists(username) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Verificar usuario")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (state is ForgotPasswordViewModel.ForgotPasswordStateUI.UserFound) {
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("Nueva contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { vm.updatePassword(username, newPassword) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Actualizar contraseña")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onBackToLogin() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al login")
        }

        WhatsappButton()


        // Mostrar mensajes de estado
        when (val st = state) {
            is ForgotPasswordViewModel.ForgotPasswordStateUI.Loading -> Text("Verificando usuario...")
            is ForgotPasswordViewModel.ForgotPasswordStateUI.Updating -> Text("Actualizando contraseña...")
            is ForgotPasswordViewModel.ForgotPasswordStateUI.UserNotFound ->
                Text(st.message, color = MaterialTheme.colorScheme.error)
            is ForgotPasswordViewModel.ForgotPasswordStateUI.UpdateSuccess ->
                Text("Contraseña actualizada con éxito", color = MaterialTheme.colorScheme.primary)
            is ForgotPasswordViewModel.ForgotPasswordStateUI.UpdateError ->
                Text(st.message, color = MaterialTheme.colorScheme.error)
            else -> {}
        }




    }
}

