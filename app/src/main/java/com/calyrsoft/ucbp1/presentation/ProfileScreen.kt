package com.calyrsoft.ucbp1.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    name: String,
    vm: ProfileViewModel = koinViewModel(),
    onEndSession: () -> Unit,
    onAskExchangeRate: () -> Unit
) {
    val state by vm.state.collectAsState()

    var editableName by remember { mutableStateOf(name) }
    var editablePhone by remember { mutableStateOf("") }
    var editablePassword by remember { mutableStateOf("") }
    var editableImageUrl by remember { mutableStateOf("") }

    // Cargar datos del perfil al iniciar la pantalla
    LaunchedEffect(name) {
        vm.loadProfile(name)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when(val st = state) {
            is ProfileViewModel.ProfileStateUI.Init -> Text("Inicializando...")
            is ProfileViewModel.ProfileStateUI.Loading -> Text("Cargando perfil...")

            is ProfileViewModel.ProfileStateUI.DataLoaded -> {
                val user = st.user

                if (editableName.isEmpty()) editableName = user.name
                if (editablePhone.isEmpty()) editablePhone = user.phone
                if (editablePassword.isEmpty()) editablePassword = user.password
                if (editableImageUrl.isEmpty()) editableImageUrl = user.imageUrl

                OutlinedTextField(
                    value = editableName,
                    onValueChange = { editableName = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = editablePhone,
                    onValueChange = { editablePhone = it },
                    label = { Text("Teléfono") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = editablePassword,
                    onValueChange = { editablePassword = it },
                    label = { Text("Contraseña") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = editableImageUrl,
                    onValueChange = { editableImageUrl = it },
                    label = { Text("URL de imagen") },
                    modifier = Modifier.fillMaxWidth()
                )

                AsyncImage(
                    model = editableImageUrl,
                    contentDescription = "Imagen de perfil",
                    modifier = Modifier
                        .size(120.dp)
                        .padding(vertical = 16.dp),
                    contentScale = ContentScale.Crop
                )

                Button(
                    onClick = {
                        val newName = if (editableName != user.name) editableName else null
                        val newPhone = if (editablePhone != user.phone) editablePhone else null
                        val newPassword = if (editablePassword != user.password) editablePassword else null
                        val newImageUrl = if (editableImageUrl != user.imageUrl) editableImageUrl else null

                        // Solo enviamos los valores modificados
                        vm.updateProfile(
                            name = user.name,
                            newName = newName,
                            newPhone = newPhone,
                            newImageUrl = newImageUrl,
                            newPassword = newPassword
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar cambios")
                }

                OutlinedButton(
                    onClick = { onEndSession() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cerrar sesión")
                }

                OutlinedButton(
                    onClick = { onAskExchangeRate() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Tasa de cambio")
                }
            }

            is ProfileViewModel.ProfileStateUI.Updating -> Text("Actualizando...")
            is ProfileViewModel.ProfileStateUI.UpdateSuccess -> {
                Text("Actualizacion exitosa")

                vm.loadProfile(st.user.name)
            }
            is ProfileViewModel.ProfileStateUI.UpdateError -> Text("Error: ${(st as ProfileViewModel.ProfileStateUI.UpdateError).message}")
        }
    }
}
