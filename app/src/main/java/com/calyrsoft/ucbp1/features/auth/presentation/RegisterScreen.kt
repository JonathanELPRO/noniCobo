package com.calyrsoft.ucbp1.features.auth.presentation

import androidx.compose.foundation.*
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
import com.calyrsoft.ucbp1.features.auth.domain.model.Role

@Composable
fun RegisterScreen(
    vm: RegisterViewModel,
    onRegisterSuccess: () -> Unit = {},
    onBackToLogin: () -> Unit = {}
) {
    val state by vm.state.collectAsState()
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(Role.CLIENT) }

    val scrollState = rememberScrollState()

    Scaffold(containerColor = Color(0xFFF4F4F4)) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔹 Cabecera visual
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
                    modifier = Modifier
                        .size(180.dp)
                        .clip(RoundedCornerShape(90.dp))
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Crear cuenta", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))

            // 🔹 Selección de rol
            Text("Selecciona tu tipo de usuario:", color = Color.DarkGray, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                RoleOptionCard("Cliente", selectedRole == Role.CLIENT) { selectedRole = Role.CLIENT }
                RoleOptionCard("Administrador", selectedRole == Role.ADMIN) { selectedRole = Role.ADMIN }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Campos de registro
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Nombre de usuario") },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Teléfono (opcional)") },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Estado de la UI
            when (val st = state) {
                is RegisterViewModel.RegisterUIState.Loading -> CircularProgressIndicator(color = Color(0xFFB00020))
                is RegisterViewModel.RegisterUIState.Error -> Text(st.message, color = MaterialTheme.colorScheme.error)
                is RegisterViewModel.RegisterUIState.Success -> {
                    onRegisterSuccess()
                    vm.resetState() // Descomentar si necesitas limpiar el estado tras navegar
                }
                else -> Unit
            }

            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    vm.register(
                        username,
                        email,
                        phone.ifBlank { null },
                        password,
                        selectedRole
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Registrar", color = Color.White, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(12.dp))
            TextButton(onClick = onBackToLogin) {
                Text("¿Ya tienes cuenta? Inicia sesión", color = Color.DarkGray)
            }
        }
    }
}

@Composable
private fun RoleOptionCard(title: String, isSelected: Boolean, onClick: () -> Unit) {
    val background = if (isSelected) Color(0xFFB00020) else Color(0xFFE0E0E0)
    val textColor = if (isSelected) Color.White else Color.Black

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(background)
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(title, color = textColor, fontWeight = FontWeight.Medium)
    }
}
