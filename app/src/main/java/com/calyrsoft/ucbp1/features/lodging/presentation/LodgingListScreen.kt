package com.calyrsoft.ucbp1.features.lodging.presentation

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
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
import com.calyrsoft.ucbp1.dataStore.AuthDataStore
import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import com.calyrsoft.ucbp1.features.lodging.domain.model.RoomCategory
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LodgingListScreen(
    vm: LodgingListViewModel = koinViewModel(),
    onDetails: (Long) -> Unit = {},
    onBack: () -> Unit = {},
    authDataStore: AuthDataStore
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) { vm.load() }

    var role by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val result = authDataStore.getRole()
        if (result.isSuccess) {
            role = result.getOrNull() ?: ""
        }
    }


    Scaffold(
        containerColor = Color(0xFFF4F4F4),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(
                        Color(0xFFB00020),
                        shape = RoundedCornerShape(bottomStart = 80.dp, bottomEnd = 80.dp)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 30.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                    Text(
                        "ALOJAMIENTOS",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(48.dp))
                }
            }
        }
    ) { padding ->

        when(val st = state) {
            is LodgingListViewModel.LodgingListStateUI.Init,
            is LodgingListViewModel.LodgingListStateUI.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFB00020))
                }
            }

            is LodgingListViewModel.LodgingListStateUI.Success -> {
                val lodgings = st.lodgings

                if (lodgings.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay alojamientos registrados..", color = Color.Gray)

                        Text(text = role,color = Color.Gray)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(horizontal = 16.dp)
                    ) {
                        items(lodgings) { lodging ->
                            LodgingCard(lodging, onDetails)
                        }
                    }
                }
            }

            is LodgingListViewModel.LodgingListStateUI.Error -> {
                val message = st.message
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("⚠️ Error", fontWeight = FontWeight.Bold, color = Color.Red)
                        Text(message, color = Color.DarkGray)
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = { vm.load() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020))
                        ) {
                            Text("Reintentar", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LodgingCard(lodging: Lodging, onDetails: (Long) -> Unit) {
    // 🔍 Precio dinámico: habitación simple o primer precio
    val simplePrice = lodging.roomOptions.find { it.category == RoomCategory.SIMPLE }?.price
        ?: lodging.roomOptions.firstOrNull()?.price
        ?: 0.0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // --- Imagen miniatura ---
            val imageUri = lodging.placeImageUri
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF0F0F0)),
                contentAlignment = Alignment.Center
            ) {
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
                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(10.dp))
                    )
                } else {
                    Icon(Icons.Default.Image, contentDescription = "Sin imagen", tint = Color.Gray)
                }
            }

            Spacer(Modifier.width(12.dp))

            // --- Información textual ---
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(lodging.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(lodging.type.name, color = Color(0xFFB00020), fontWeight = FontWeight.Medium)
                Text("Bs/H. $simplePrice", color = Color(0xFFB00020), fontWeight = FontWeight.SemiBold)
                lodging.district?.let {
                    Text("Zona: $it", color = Color.DarkGray, fontSize = 13.sp)
                }
            }

            Spacer(Modifier.width(8.dp))

            // --- Botón "Ver" ---
            Button(
                onClick = { lodging.id?.let(onDetails) },
                //“Si lodging.id no es nulo, ejecuta la función onDetails() y pásale ese id como parámetro”.
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020)),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text("Ver", color = Color.White)
            }
        }
    }
}
