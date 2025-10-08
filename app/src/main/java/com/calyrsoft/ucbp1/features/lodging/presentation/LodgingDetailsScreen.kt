package com.calyrsoft.ucbp1.features.lodging.presentation

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LodgingDetailsScreen(
    id: Long,
    vm: LodgingDetailsViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onBack: () -> Unit = {}
) {
    val lodging by vm.lodging.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(id) { scope.launch { vm.load(id) } }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(lodging?.name ?: "Detalles del alojamiento") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        lodging?.let { l ->
            val scroll = rememberScrollState()  // 👈 agregado
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(scroll),  // 👈 permite desplazamiento
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Tipo: ${l.type}")
                Text("Dirección: ${l.address ?: "-"}")
                Text("Zona: ${l.district ?: "-"}")
                Text("Teléfono: ${l.contactPhone ?: "-"}")
                Text("Atiende 24h: ${if (l.open24h) "Sí" else "No"}")
                Text("Latitud: ${l.latitude ?: "-"}")
                Text("Longitud: ${l.longitude ?: "-"}")

                Divider(thickness = 1.dp)

                l.placeImage?.let {
                    val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
                    Image(
                        bitmap = bmp.asImageBitmap(),
                        contentDescription = "Lugar",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )
                }

                l.licenseImage?.let {
                    val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
                    Image(
                        bitmap = bmp.asImageBitmap(),
                        contentDescription = "Licencia",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )
                }

                Divider(thickness = 1.dp)
                Text("Precios por estadía:", style = MaterialTheme.typography.titleMedium)
                l.stayOptions.forEach { opt ->
                    Text("• ${opt.type.name.replace('_', ' ')} - Bs ${"%.2f".format(opt.price)}")
                }

                Divider(thickness = 1.dp)
                Text("Tipos de habitación:", style = MaterialTheme.typography.titleMedium)
                l.roomOptions.forEach { opt ->
                    Text("• ${opt.category.name.replace('_', ' ')} - Bs ${"%.2f".format(opt.price)}")
                }
            }
        } ?: Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
