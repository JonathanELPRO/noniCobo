package com.calyrsoft.ucbp1.features.lodging.presentation

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.calyrsoft.ucbp1.features.auth.domain.model.Role
import com.calyrsoft.ucbp1.features.auth.presentation.AuthViewModel
import com.calyrsoft.ucbp1.features.lodging.domain.model.*
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LodgingEditorScreen(
    currentRole: Role,
    vm: LodgingEditorViewModel = koinViewModel(),
    lodgingToEdit: Lodging? = null,
    onSaved: () -> Unit = {}
) {



    Log.d("LodgingEditorScreen", "llegue a el editor")
    val authViewModel: AuthViewModel = getViewModel()

    var phone by remember { mutableStateOf("") }


    LaunchedEffect(Unit) {
        val fetchedPhone = authViewModel.getPhone()
        if (fetchedPhone != null) {
            phone = fetchedPhone
        }

    }

    val userId by authViewModel.userId.collectAsState()
    val state by vm.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }



    // Variables de formulario inicializadas según si es edición o creación
    var name by rememberSaveable { mutableStateOf(lodgingToEdit?.name ?: "") }
    var type by rememberSaveable { mutableStateOf(lodgingToEdit?.type ?: LodgingType.MOTEL) }
    var district by rememberSaveable { mutableStateOf(lodgingToEdit?.district ?: "") }
    var address by rememberSaveable { mutableStateOf(lodgingToEdit?.address ?: "") }
    var contact by rememberSaveable { mutableStateOf(lodgingToEdit?.contactPhone ?:phone) }

    LaunchedEffect(phone) {
        if (lodgingToEdit == null && contact.isEmpty()) {
            contact = phone
        }
    }

    Log.d("phone", phone)
    var open24h by rememberSaveable { mutableStateOf(lodgingToEdit?.open24h ?: false) }
    var latitude by rememberSaveable { mutableStateOf(lodgingToEdit?.latitude?.toString() ?: "") }
    var longitude by rememberSaveable { mutableStateOf(lodgingToEdit?.longitude?.toString() ?: "") }
    var placeImageUri by rememberSaveable { mutableStateOf(lodgingToEdit?.placeImageUri) }
    var licenseImageUri by rememberSaveable { mutableStateOf(lodgingToEdit?.licenseImageUri) }

    // Precios según roomOptions
    var priceSimple by rememberSaveable {
        mutableStateOf(
            lodgingToEdit?.roomOptions?.find { it.category == RoomCategory.SIMPLE }?.price?.toString() ?: ""
        )
    }
    var priceBanio by rememberSaveable {
        mutableStateOf(
            lodgingToEdit?.roomOptions?.find { it.category == RoomCategory.CON_BAÑO }?.price?.toString() ?: ""
        )
    }
    var priceBanioTV by rememberSaveable {
        mutableStateOf(
            lodgingToEdit?.roomOptions?.find { it.category == RoomCategory.CON_BAÑO_Y_TV }?.price?.toString() ?: ""
        )
    }

    // Lanzadores de imágenes
    val launcherPlace = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { placeImageUri = it.toString() }
    }
    val launcherLicense = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { licenseImageUri = it.toString() }
    }

    LaunchedEffect(state) {
        if (state is LodgingEditorViewModel.LodgingEditorStateUI.UpdateSuccess) {
            snackbarHostState.showSnackbar("Alojamiento guardado correctamente.")
            onSaved()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFF4F4F4)
    ) { padding ->
        val scroll = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding() )
                .verticalScroll(scroll)
        ) {
            // Encabezado
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(Color(0xFFB00020), shape = RoundedCornerShape(bottomStart = 80.dp, bottomEnd = 80.dp)),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(  text = if (lodgingToEdit!=null) "EDITAR" else "REGISTRAR", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color.White)
                    Spacer(Modifier.height(8.dp))
                    Icon(Icons.Default.AccountCircle, contentDescription = "Perfil", tint = Color.White, modifier = Modifier.size(50.dp))
                }
            }

            // Contenido según estado
            when (state) {
                is LodgingEditorViewModel.LodgingEditorStateUI.Init -> {
                    FormContent(
                        name = name, onNameChange = { name = it },
                        type = type, onTypeChange = { type = it },
                        district = district, onDistrictChange = { district = it },
                        address = address, onAddressChange = { address = it },
                        contact = contact, onContactChange = { contact = it },
                        latitude = latitude, onLatitudeChange = { latitude = it },
                        longitude = longitude, onLongitudeChange = { longitude = it },
                        open24h = open24h, onOpen24hChange = { open24h = it },
                        placeImageUri = placeImageUri, onPickPlaceImage = { launcherPlace.launch("image/*") },
                        licenseImageUri = licenseImageUri, onPickLicenseImage = { launcherLicense.launch("image/*") },
                        priceSimple = priceSimple, onPriceSimpleChange = { priceSimple = it },
                        priceBanio = priceBanio, onPriceBanioChange = { priceBanio = it },
                        priceBanioTV = priceBanioTV, onPriceBanioTVChange = { priceBanioTV = it },
                        onSaveClick = {
                            val rooms = listOf(
                                RoomOption(RoomCategory.SIMPLE, priceSimple.toDoubleOrNull() ?: 0.0),
                                RoomOption(RoomCategory.CON_BAÑO, priceBanio.toDoubleOrNull() ?: 0.0),
                                RoomOption(RoomCategory.CON_BAÑO_Y_TV, priceBanioTV.toDoubleOrNull() ?: 0.0)
                            )

                            val lodging = Lodging(
                                id = lodgingToEdit?.id,
                                name = name,
                                type = type,
                                district = district,
                                address = address,
                                contactPhone = contact,
                                open24h = open24h,
                                ownerAdminId = userId ?: 0,
                                latitude = latitude.toDoubleOrNull(),
                                longitude = longitude.toDoubleOrNull(),
                                roomOptions = rooms,
                                stayOptions = listOf(
                                    StayOption(StayType.POR_HORA),
                                    StayOption(StayType.POR_NOCHE),
                                    StayOption(StayType.POR_DIA)
                                ),
                                placeImageUri = placeImageUri,
                                licenseImageUri = licenseImageUri
                            )
                            vm.save(currentRole, lodging)
                        },
                        isSaving = state is LodgingEditorViewModel.LodgingEditorStateUI.Updating
                    )
                }
                is LodgingEditorViewModel.LodgingEditorStateUI.Updating -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFFB00020))
                    }
                }
                is LodgingEditorViewModel.LodgingEditorStateUI.UpdateSuccess -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Guardado correctamente", color = Color(0xFF388E3C), fontSize = 18.sp)
                    }
                }
                is LodgingEditorViewModel.LodgingEditorStateUI.UpdateError -> {
                    val error = (state as LodgingEditorViewModel.LodgingEditorStateUI.UpdateError).message
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error: $error", color = Color.Red, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun FormContent(
    name: String, onNameChange: (String) -> Unit,
    type: LodgingType, onTypeChange: (LodgingType) -> Unit,
    district: String, onDistrictChange: (String) -> Unit,
    address: String, onAddressChange: (String) -> Unit,
    contact: String, onContactChange: (String) -> Unit,
    //roomsCount: String, onRoomsCountChange: (String) -> Unit,
    latitude: String, onLatitudeChange: (String) -> Unit,
    longitude: String, onLongitudeChange: (String) -> Unit,
    open24h: Boolean, onOpen24hChange: (Boolean) -> Unit,
    placeImageUri: String?, onPickPlaceImage: () -> Unit,
    licenseImageUri: String?, onPickLicenseImage: () -> Unit,
    priceSimple: String, onPriceSimpleChange: (String) -> Unit,
    priceBanio: String, onPriceBanioChange: (String) -> Unit,
    priceBanioTV: String, onPriceBanioTVChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    isSaving: Boolean
) {
    Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Tipo de Servicio:", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            TypeChip(selected = type == LodgingType.MOTEL, text = "Motel") { onTypeChange(LodgingType.MOTEL) }
            TypeChip(selected = type == LodgingType.RESIDENCIAL, text = "Residencial") { onTypeChange(LodgingType.RESIDENCIAL) }
        }

        OutlinedTextField(value = name, onValueChange = onNameChange, label = { Text("Nombre del Alojamiento") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
        //OutlinedTextField(value = roomsCount, onValueChange = onRoomsCountChange, label = { Text("Número de Habitaciones") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
        OutlinedTextField(value = contact, onValueChange = onContactChange, label = { Text("Número de Contacto") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
        OutlinedTextField(value = district, onValueChange = onDistrictChange, label = { Text("Zona o Barrio") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
        OutlinedTextField(value = address, onValueChange = onAddressChange, label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))

        HorizontalDivider(color = Color.LightGray, thickness = 1.dp)

        Text("Coordenadas:", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
        OutlinedTextField(value = latitude, onValueChange = onLatitudeChange, label = { Text("Latitud") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = longitude, onValueChange = onLongitudeChange, label = { Text("Longitud") }, modifier = Modifier.fillMaxWidth())

        HorizontalDivider(color = Color.LightGray, thickness = 1.dp)

        Text("Imágenes:", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
        UploadImageButton(uri = placeImageUri, text = "Imagen del lugar", onClick = onPickPlaceImage)
        UploadImageButton(uri = licenseImageUri, text = "Licencia de Funcionamiento", onClick = onPickLicenseImage)

        HorizontalDivider(color = Color.LightGray, thickness = 1.dp)

        Text("Tipo de Habitación:", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
        PriceField("Simple", priceSimple, onPriceSimpleChange)
        PriceField("Baño privado", priceBanio, onPriceBanioChange)
        PriceField("Baño privado y TV", priceBanioTV, onPriceBanioTVChange)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = open24h, onCheckedChange = onOpen24hChange)
            Text("Atiende 24 horas")
        }

        Button(
            onClick = onSaveClick,
            enabled = !isSaving,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020)),
            modifier = Modifier.fillMaxWidth().height(50.dp).clip(RoundedCornerShape(14.dp))
        ) {
            Text(if (isSaving) "Guardando..." else "Guardar", color = Color.White, fontSize = 16.sp)
        }
    }
}

@Composable
private fun UploadImageButton(uri: String?, text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020)),
        modifier = Modifier.fillMaxWidth().height(120.dp).clip(RoundedCornerShape(16.dp))
    ) {
        if (uri == null) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Image, contentDescription = null, tint = Color.White)
                Text(text, color = Color.White)
            }
        } else {
            Image(
                painter = rememberAsyncImagePainter(Uri.parse(uri)),
                //“Convierte la dirección del archivo (uri) en una imagen visible dentro del Composable, cargándola sin bloquear la app.”
                //Uri.parse(uri)
                //Convierte un texto en un objeto URI válido.
                contentDescription = text,
                modifier = Modifier.fillMaxWidth().height(120.dp).clip(RoundedCornerShape(16.dp))
            )
        }
    }
}

@Composable
private fun PriceField(label: String, value: String, onChange: (String) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(value = value, onValueChange = onChange, label = { Text(label) }, shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f))
        Spacer(Modifier.width(8.dp))
        Text("Bs.", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
    }
}

@Composable
private fun TypeChip(selected: Boolean, text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) Color(0xFFB00020) else Color(0xFFE0E0E0),
            contentColor = if (selected) Color.White else Color.Black
        ),
        shape = RoundedCornerShape(50),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
    ) {
        Text(text, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}
