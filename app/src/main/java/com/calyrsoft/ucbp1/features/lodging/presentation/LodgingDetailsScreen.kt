package com.calyrsoft.ucbp1.features.lodging.presentation

import android.app.TimePickerDialog
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.calyrsoft.ucbp1.features.auth.presentation.AuthViewModel
import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import com.calyrsoft.ucbp1.features.lodging.domain.model.RoomCategory
import com.calyrsoft.ucbp1.features.lodging.domain.model.StayType
import com.calyrsoft.ucbp1.features.payments.domain.model.PaymentModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LodgingDetailsScreen(
    id: Long,
    vm: LodgingDetailsViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onBack: () -> Unit = {},
    onPagos: (PaymentModel) -> Unit = {},
    onResenas: () -> Unit = {}
) {
    val state by vm.state.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(id) { scope.launch { vm.load(id) } }

    Scaffold(containerColor = Color(0xFFFDFDFD)) { padding ->

        when(val st = state) {
            is LodgingDetailsViewModel.LodgingDetailsStateUI.Init,
            is LodgingDetailsViewModel.LodgingDetailsStateUI.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFB00020))
                }
            }

            is LodgingDetailsViewModel.LodgingDetailsStateUI.Error -> {
                val message = st.message
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("⚠️ Error", color = Color.Red, fontWeight = FontWeight.Bold)
                        Text(message, color = Color.DarkGray)
                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = { vm.load(id) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020))
                        ) {
                            Text("Reintentar", color = Color.White)
                        }
                    }
                }
            }

            is LodgingDetailsViewModel.LodgingDetailsStateUI.Success -> {
                val lodging = st.lodging
                LodgingDetailsContent(
                    lodging = lodging,
                    onBack = onBack,
                    onPagos = onPagos,
                    onResenas = onResenas,
                    padding = padding
                )
            }
        }
    }
}

@Composable
private fun LodgingDetailsContent(
    lodging: Lodging,
    onBack: () -> Unit,
    onPagos: (PaymentModel) -> Unit,
    onResenas: () -> Unit,
    padding: PaddingValues
) {
    val scroll = rememberScrollState()
    val context = LocalContext.current

    val authViewModel: AuthViewModel = getViewModel()

    var username by remember { mutableStateOf("Invitado") }

    LaunchedEffect(Unit) {
        val fetchedName = authViewModel.getUsername()
        if (fetchedName != null) {
            username = fetchedName
        }
    }


    var selectedRoom by remember {
        mutableStateOf(lodging.roomOptions.firstOrNull()?.category)
    }

    var selectedStay by remember {
        mutableStateOf(lodging.stayOptions.firstOrNull()?.type)
    }

    var hours by remember { mutableStateOf("1") }
    var arrivalTime by remember { mutableStateOf("") }

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour: Int, minute: Int ->
            arrivalTime = String.format("%02d:%02d", hour, minute)
        },
        12, //hora inicial
        0, //minuto inicial
        true //formato de 24 horas del relog
    )
    //Sirve para mostrar una ventana emergente (reloj) donde el usuario puede elegir
    // una hora y un minuto, y luego guardar ese valor en una variable (arrivalTime).

    // Precio dinámico según selección
    val selectedPrice by remember(selectedRoom, selectedStay, hours) {
        derivedStateOf {
            // Precio base de la habitación seleccionada (o primera si no hay selección)
            val basePrice = lodging.roomOptions.find { it.category == selectedRoom }?.price
                ?: lodging.roomOptions.firstOrNull()?.price
                ?: 0.0

            // Determinar precio según tipo de estadía
            when (selectedStay) {
                StayType.POR_HORA -> {
                    val h = hours.toDoubleOrNull() ?: 1.0
                    basePrice * h
                }
                StayType.POR_NOCHE -> {
                    val price = basePrice * 6
                    price + (price * 0.10) // +10% por la noche
                }
                StayType.POR_DIA -> {
                    val price = basePrice * 24
                    price - (price * 0.10) // -10% por descuento del día completo
                }
                else -> basePrice
            }
        }
    }

    //en selected price se guarda un valor que depende de lo que digamos entre {}
    //pero ese valor cambiara a cada rato si es que selectedRoom cambia
    //derivedStateOf significa vamos a cambiar el selectedPrice
    //en base a selectedRoom, imagina que selectedRoomCambia
    //ese valor llega a derivedStateOf, entramos a lodging que tiene un atributo roomOptions
    //y este tiene una lista de objetos javascript, donde cada objeto tiene una categoria de cuarot y un precio
    //primero buscamos a aquel objeto que su categoria coincide con la habitacion que selecciono el usuario
    //accedemos a su precio, si no podemos acceder a su precio, pues tomamos el precio de la primera categoria osea:                 ?: lodging.roomOptions.firstOrNull()?.price
    //y si ni a esa podemos pues le ponemos precio de cero osea:                 ?: 0.0

    // Prefijo dinámico según tipo de estadía
    val stayPrefix by remember(selectedStay) {
        derivedStateOf {
            when (selectedStay) {
                StayType.POR_HORA -> "Bs."
                StayType.POR_NOCHE -> "Bs/N."
                StayType.POR_DIA -> "Bs/D."
                else -> "Bs."
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = padding.calculateBottomPadding() )
            .verticalScroll(scroll)
    ) {
        // Encabezado con imagen
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(
                    Color(0xFFB00020),
                    shape = RoundedCornerShape(bottomStart = 80.dp, bottomEnd = 80.dp)
                )
        ) {
            IconButton(onClick = onBack, modifier = Modifier.padding(16.dp).align(Alignment.TopStart)) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
            }

            val imageUri = lodging.placeImageUri
            if (!imageUri.isNullOrEmpty()) {
                val painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(
                            if (imageUri.startsWith("content://") || imageUri.startsWith("file://"))
                                Uri.parse(imageUri)
                            else imageUri
                        )
                        .crossfade(true)
                        .allowHardware(false)
                        .build()
                )

                Image(
                    painter = painter,
                    contentDescription = "Imagen del alojamiento",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp)
                        .clip(RoundedCornerShape(bottomStart = 80.dp, bottomEnd = 80.dp))
                        .align(Alignment.BottomCenter)
                )
            } else {
                // Placeholder si no hay imagen
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp)
                        .clip(RoundedCornerShape(bottomStart = 80.dp, bottomEnd = 80.dp))
                        .align(Alignment.BottomCenter)
                        .background(Color(0xFFB00020).copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Image,
                        contentDescription = "Sin imagen",
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(64.dp)
                    )
                }
            }
        }

        // Contenido de detalles
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(lodging.name, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Text(
                "$stayPrefix $selectedPrice",
                color = Color(0xFFB00020),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
            Divider()

            // Tipo de habitación
            Text("Habitación", fontWeight = FontWeight.SemiBold)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                lodging.roomOptions.forEach { option ->
                    SelectableChip(
                        text = option.category.name.replace("_", " "),
                        selected = selectedRoom == option.category,
                        onClick = { selectedRoom = option.category }
                    )
                }
            }

            // Tipo de estadía
            Text("Tipo de estadía", fontWeight = FontWeight.SemiBold)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                lodging.stayOptions.forEach { option ->
                    SelectableChip(
                        text = option.type.name.replace("_", " "),
                        selected = selectedStay == option.type,
                        onClick = { selectedStay = option.type }
                    )
                }
            }

            // Mostrar campo de horas solo si es por hora
            AnimatedVisibility(visible = selectedStay == StayType.POR_HORA) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = hours,
                        onValueChange = { hours = it },
                        label = { Text("Horas") },
                        modifier = Modifier.weight(1f)
                    )
                    Text(" Hr.", modifier = Modifier.padding(start = 4.dp))
                }
            }

            // Hora de llegada
            Text("Hora de llegada", fontWeight = FontWeight.SemiBold)
            OutlinedButton(
                onClick = { timePickerDialog.show() },
                shape = RoundedCornerShape(12.dp),
                border = ButtonDefaults.outlinedButtonBorder,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.AccessTime, contentDescription = null, tint = Color(0xFFB00020))
                Spacer(Modifier.width(8.dp))
                Text(if (arrivalTime.isNotEmpty()) arrivalTime else "Seleccionar hora")
            }

            Divider()

            // Información adicional
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Phone, contentDescription = null, tint = Color(0xFFB00020))
                Spacer(Modifier.width(8.dp))
                Text(lodging.contactPhone ?: "-")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFFB00020))
                Spacer(Modifier.width(8.dp))
                Text(lodging.address ?: lodging.district ?: "-")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AccessTimeFilled, contentDescription = null, tint = Color(0xFFB00020))
                Spacer(Modifier.width(8.dp))
                Text(if (lodging.open24h) "Atiende 24/7" else "Horario limitado")
            }

            Spacer(Modifier.height(16.dp))

            // Botones inferiores
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onResenas,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Reseñas", color = Color.White)
                }


                Button(
                    onClick = {
                        val payment = PaymentModel(
                            lodgingName = lodging.name,
                            userName = username,
                            selectedRoom = selectedRoom,
                            selectedStay = selectedStay,
                            selectedPrice = selectedPrice,
                            arrivalTime = arrivalTime,
                            hours = if (selectedStay == StayType.POR_HORA) hours else null,
                            ownerNumber = lodging.contactPhone
                        )
                        onPagos(payment)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Pagar", color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun SelectableChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(50),
        color = if (selected) Color(0xFFB00020) else Color(0xFFE0E0E0),
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .border(
                width = 1.dp,
                color = if (selected) Color(0xFFB00020) else Color.LightGray,
                shape = RoundedCornerShape(50)
            )
    ) {
        TextButton(
            onClick = onClick,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Text(
                text = text,
                color = if (selected) Color.White else Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
