package com.calyrsoft.ucbp1.features.profile.presentation

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
    var editableEmail by remember { mutableStateOf("") }
    var editableSummary by remember { mutableStateOf("") }


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

                if (editableName.isEmpty()) editableName = user.name.value
                if (editablePhone.isEmpty()) editablePhone = user.phone.value
                if (editablePassword.isEmpty()) editablePassword = user.password.value
                if (editableImageUrl.isEmpty()) editableImageUrl = user.imageUrl.value
                if (editableEmail.isEmpty()) editableEmail = user.email.value
                if (editableSummary.isEmpty()) editableSummary = user.summary.value



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
                    value = editableEmail,
                    onValueChange = { editableEmail = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = editableSummary,
                    onValueChange = { editableSummary = it },
                    label = { Text("Summary") },
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
                        val newName = if (editableName != user.name.value) editableName else null
                        val newPhone = if (editablePhone != user.phone.value) editablePhone else null
                        val newPassword = if (editablePassword != user.password.value) editablePassword else null
                        val newImageUrl = if (editableImageUrl != user.imageUrl.value) editableImageUrl else null
                        val newEmail = if (editableEmail != user.email.value) editableEmail else null
                        val newSummary = if (editableSummary != user.summary.value) editableSummary else null


                        vm.updateProfile(
                            name = user.name.value,
                            newName = newName,
                            newPhone = newPhone,
                            newImageUrl = newImageUrl,
                            newPassword = newPassword,
                            newEmail = newEmail,
                            newSummary = newSummary
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

                vm.loadProfile(st.user.name.value)
            }
            is ProfileViewModel.ProfileStateUI.UpdateError -> Text("Error: ${(st as ProfileViewModel.ProfileStateUI.UpdateError).message}")
        }
    }
}
