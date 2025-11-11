package com.calyrsoft.ucbp1.features.profile.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.calyrsoft.ucbp1.features.auth.presentation.AuthViewModel
import org.koin.androidx.compose.getViewModel

// Paleta de colores de la imagen
private val BurgundyRed = Color(0xFF8B1A1A)
private val LightBlue = Color(0xFFADD8E6)
private val Black = Color(0xFF000000)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    vm : ProfileViewModel
) {
    var isEditing by remember { mutableStateOf(false) }
    val authViewModel: AuthViewModel = getViewModel()
    val snackbarHostState = remember { SnackbarHostState() }
    val userEmail by authViewModel.userEmail.collectAsState()
    val userId by authViewModel.userId.collectAsState()
    val userToken by authViewModel.userToken.collectAsState()



    LaunchedEffect(userEmail) {
        if (!userEmail.isNullOrBlank()) {
            vm.loadProfile(userEmail!!)
        }
    }

    val profileState by vm.state.collectAsState()

    var editableUsername by remember { mutableStateOf("") }
    var editablePhone by remember { mutableStateOf("") }
    var editablePassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var originalUsername by remember { mutableStateOf("") }
    var originalPhone by remember { mutableStateOf("") }

// Cuando se cargue el usuario, llenar los campos editables
    LaunchedEffect(profileState) {
        if (profileState is ProfileViewModel.ProfileStateUI.DataLoaded) {
            val user = (profileState as ProfileViewModel.ProfileStateUI.DataLoaded).user
            editableUsername = (user.username.value ?: "") as String
            editablePhone = (user.phone?.value ?: "") as String
            originalUsername = (user.username.value ?: "") as String
            originalPhone = (user.phone?.value ?: "") as String
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Mi Perfil",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BurgundyRed
                ),
                actions = {
                    IconButton(onClick = {
                        if (isEditing) {
                            // Cancelar
                            editableUsername = originalUsername
                            editablePhone = originalPhone
                        }
                        isEditing = !isEditing
                    }) {
                        Icon(
                            imageVector = if (isEditing) Icons.Default.Close else Icons.Default.Edit,
                            contentDescription = if (isEditing) "Cancelar" else "Editar",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Iniciales en círculo

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(LightBlue, shape = RoundedCornerShape(60.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = editableUsername.take(2).uppercase(),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Black
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = userEmail ?: "",
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )
            }



            // Campos del perfil
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = "Información Personal",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = BurgundyRed
                    )

                    // Nombre de usuario
                    ProfileTextField(
                        value = editableUsername,
                        onValueChange = { editableUsername = it },
                        label = "Nombre de usuario",
                        icon = Icons.Default.Person,
                        enabled = isEditing
                    )

                    // Teléfono
                    ProfileTextField(
                        value = editablePhone,
                        onValueChange = { editablePhone = it },
                        label = "Teléfono",
                        icon = Icons.Default.Phone,
                        enabled = isEditing,
                        keyboardType = KeyboardType.Phone
                    )

                    // Contraseña
                    OutlinedTextField(
                        value = editablePassword,
                        onValueChange = { editablePassword = it },
                        label = { Text("Contraseña") },
                        placeholder = { Text("Ingresa nueva contraseña") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = null,
                                tint = if (isEditing) BurgundyRed else Color.Gray
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    imageVector = if (showPassword) Icons.Default.Visibility
                                    else Icons.Default.VisibilityOff,
                                    contentDescription = if (showPassword) "Ocultar" else "Mostrar",
                                    tint = if (isEditing) BurgundyRed else Color.Gray
                                )
                            }
                        },
                        visualTransformation = if (showPassword) androidx.compose.ui.text.input.VisualTransformation.None
                        else androidx.compose.ui.text.input.PasswordVisualTransformation(),
                        enabled = isEditing,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BurgundyRed,
                            focusedLabelColor = BurgundyRed,
                            cursorColor = BurgundyRed,
                            disabledBorderColor = Color.LightGray,
                            disabledTextColor = Color.DarkGray,
                            disabledLabelColor = Color.Gray
                        )
                    )
                }
            }

            // Botones
            if (isEditing) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            isEditing = false
                            editableUsername = originalUsername
                            editablePhone = originalPhone
                            editablePassword = ""
                        },
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = BurgundyRed)
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = {
                            if (userId != null) {
                                vm.updateProfile(userId?:0, editableUsername, editablePhone)
                                if(editablePassword!=""){
                                    Log.d("ProfileScreen","Updating password to: $editablePassword")
                                    vm.updateUserPassword( editablePassword,userToken?:"")
                                }
                            }
                            isEditing = false
                        },
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BurgundyRed)
                    ) { Text("Guardar") }
                }
            }
        }

        LaunchedEffect(profileState) {
            when(profileState) {
                is ProfileViewModel.ProfileStateUI.UpdateSuccess -> {
                    snackbarHostState.showSnackbar("Perfil actualizado correctamente")
                }
                is ProfileViewModel.ProfileStateUI.UpdateError -> {
                    val message = (profileState as ProfileViewModel.ProfileStateUI.UpdateError).message
                    snackbarHostState.showSnackbar(message)
                }
                else -> {}
            }
        }
    }
}


@Composable
private fun ProfileTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    enabled: Boolean,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            // Elimina todos los espacios del texto automáticamente
            onValueChange(newValue.replace(" ", ""))
        },
        label = { Text(label) },
        leadingIcon = {
            Icon(
                icon,
                contentDescription = null,
                tint = if (enabled) BurgundyRed else Color.Gray
            )
        },
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BurgundyRed,
            focusedLabelColor = BurgundyRed,
            cursorColor = BurgundyRed,
            disabledBorderColor = Color.LightGray,
            disabledTextColor = Color.DarkGray,
            disabledLabelColor = Color.Gray
        )
    )
}
