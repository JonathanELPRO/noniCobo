package com.calyrsoft.ucbp1.features.lodging.presentation

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.calyrsoft.ucbp1.features.auth.domain.model.Role
import com.calyrsoft.ucbp1.features.lodging.domain.model.*
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LodgingEditorScreen(
    currentRole: Role,
    vm: LodgingEditorViewModel = koinViewModel(),
    onSaved: () -> Unit = {}
) {
    val uiState by vm.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Campos base
    var name by rememberSaveable { mutableStateOf("") }
    var type by rememberSaveable { mutableStateOf(LodgingType.MOTEL) }
    var address by rememberSaveable { mutableStateOf("") }
    var district by rememberSaveable { mutableStateOf("") }
    var contact by rememberSaveable { mutableStateOf("") }
    var open24h by rememberSaveable { mutableStateOf(false) }
    var latitude by rememberSaveable { mutableStateOf("") }
    var longitude by rememberSaveable { mutableStateOf("") }

    // Imágenes
    var placeImageBytes by remember { mutableStateOf<ByteArray?>(null) }
    var licenseImageBytes by remember { mutableStateOf<ByteArray?>(null) }

    val launcherPlace = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            placeImageBytes = context.contentResolver.openInputStream(it)?.use { input -> input.readBytes() }
        }
    }
    val launcherLicense = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            licenseImageBytes = context.contentResolver.openInputStream(it)?.use { input -> input.readBytes() }
        }
    }

    // Precios
    var pricePerHour by rememberSaveable { mutableStateOf("") }
    var pricePerNight by rememberSaveable { mutableStateOf("") }
    var pricePerDay by rememberSaveable { mutableStateOf("") }

    var priceSimple by rememberSaveable { mutableStateOf("") }
    var priceBanio by rememberSaveable { mutableStateOf("") }
    var priceBanioTV by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(uiState.success) {
        if (uiState.success) {
            snackbarHostState.showSnackbar("Alojamiento guardado correctamente.")
            onSaved()
        }
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Nuevo alojamiento") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        val scroll = rememberScrollState()
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scroll),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Información general", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
            OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Dirección") })
            OutlinedTextField(value = district, onValueChange = { district = it }, label = { Text("Zona/Barrio") })
            OutlinedTextField(value = contact, onValueChange = { contact = it }, label = { Text("Teléfono") })

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Tipo:")
                Spacer(Modifier.width(8.dp))
                DropdownMenuDemo(type) { type = it }
            }

            Divider()

            Text("Imágenes", style = MaterialTheme.typography.titleMedium)
            Button(onClick = { launcherPlace.launch("image/*") }) {
                Text("Seleccionar imagen del lugar")
            }
            placeImageBytes?.let {
                val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
                Image(bitmap = bmp.asImageBitmap(), contentDescription = "Lugar", modifier = Modifier.size(200.dp))
            }

            Button(onClick = { launcherLicense.launch("image/*") }) {
                Text("Seleccionar imagen de licencia")
            }
            licenseImageBytes?.let {
                val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
                Image(bitmap = bmp.asImageBitmap(), contentDescription = "Licencia", modifier = Modifier.size(200.dp))
            }

            Divider()

            // Tipos de estadía (solo campos)
            Text("Precios por estadía", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(value = pricePerHour, onValueChange = { pricePerHour = it }, label = { Text("Por hora (Bs)") })
            OutlinedTextField(value = pricePerNight, onValueChange = { pricePerNight = it }, label = { Text("Por noche (Bs)") })
            OutlinedTextField(value = pricePerDay, onValueChange = { pricePerDay = it }, label = { Text("Por día (Bs)") })

            Divider()

            // Tipos de habitación
            Text("Precios por tipo de habitación", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(value = priceSimple, onValueChange = { priceSimple = it }, label = { Text("Simple (Bs)") })
            OutlinedTextField(value = priceBanio, onValueChange = { priceBanio = it }, label = { Text("Con baño (Bs)") })
            OutlinedTextField(value = priceBanioTV, onValueChange = { priceBanioTV = it }, label = { Text("Con baño y TV (Bs)") })

            Divider()
            Text("Coordenadas", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(value = latitude, onValueChange = { latitude = it }, label = { Text("Latitud") })
            OutlinedTextField(value = longitude, onValueChange = { longitude = it }, label = { Text("Longitud") })

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = open24h, onCheckedChange = { open24h = it })
                Text("Atiende 24 h")
            }

            Divider()

            Button(
                onClick = {
                    val stays = listOf(
                        StayOption(StayType.POR_HORA, pricePerHour.toDoubleOrNull() ?: 0.0),
                        StayOption(StayType.POR_NOCHE, pricePerNight.toDoubleOrNull() ?: 0.0),
                        StayOption(StayType.POR_DIA, pricePerDay.toDoubleOrNull() ?: 0.0)
                    )
                    val rooms = listOf(
                        RoomOption(RoomCategory.SIMPLE, priceSimple.toDoubleOrNull() ?: 0.0),
                        RoomOption(RoomCategory.CON_BAÑO, priceBanio.toDoubleOrNull() ?: 0.0),
                        RoomOption(RoomCategory.CON_BAÑO_Y_TV, priceBanioTV.toDoubleOrNull() ?: 0.0)
                    )

                    val lodging = Lodging(
                        name = name,
                        type = type,
                        district = district,
                        address = address,
                        contactPhone = contact,
                        open24h = open24h,
                        ownerAdminId = 1L,
                        latitude = latitude.toDoubleOrNull(),
                        longitude = longitude.toDoubleOrNull(),
                        stayOptions = stays,
                        roomOptions = rooms,
                        placeImage = placeImageBytes,
                        licenseImage = licenseImageBytes
                    )
                    vm.save(currentRole, lodging)
                },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (uiState.isLoading) "Guardando..." else "Guardar")
            }

            uiState.errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
private fun DropdownMenuDemo(
    currentType: LodgingType,
    onSelect: (LodgingType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Button(onClick = { expanded = true }) {
            Text(currentType.name)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            LodgingType.values().forEach { t ->
                DropdownMenuItem(
                    text = { Text(t.name) },
                    onClick = {
                        onSelect(t)
                        expanded = false
                    }
                )
            }
        }
    }
}
