package com.calyrsoft.ucbp1.features.lodging.presentation

import LodgingSearchBar
import android.net.Uri
import android.util.Log
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.calyrsoft.ucbp1.features.auth.presentation.AuthViewModel
import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import com.calyrsoft.ucbp1.features.lodging.domain.model.RoomCategory
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LodgingListScreen(
    vm: LodgingListViewModel = koinViewModel(),
    onDetails: (Long) -> Unit = {},
    onBack: () -> Unit = {},
) {
    val authViewModel: AuthViewModel = getViewModel()
    val userId by authViewModel.userId.collectAsState()
    val userRole by authViewModel.userRole.collectAsState()

    val state by vm.state.collectAsState()

    // ★ NUEVO: estados del anuncio (url + visibilidad)
    val adUrl by vm.adUrl.collectAsState()
    val showAd by vm.showAd.collectAsState()

    // ★ NUEVO: al entrar, empezar listener y resetear visibilidad para que reaparezca
    LaunchedEffect(Unit) {
        vm.startAdListener()
        vm.resetAdVisibility()
    }


    LaunchedEffect(userId) {
        Log.d("LodgingListScreen", "Loading lodgings for userId: $userId")
        if (userId != null) {
            if (userRole == "ADMIN") {
                vm.load(userId!!)
            } else {
                vm.loadAll()
            }
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
                    Text(
                        "ALOJAMIENTOS",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    Spacer(modifier = Modifier.width(48.dp))
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // ★ NUEVO: Banner de anuncio arriba de la lista
            if (showAd && !adUrl.isNullOrBlank()) {
                AdBanner(
                    imageUrl = adUrl!!,
                    onClose = { vm.dismissAd() }
                )
                Spacer(Modifier.height(8.dp))
            }

            LodgingSearchBar(vm = vm)
            Spacer(Modifier.height(8.dp))

            when (val st = state) {
                is LodgingListViewModel.LodgingListStateUI.Init,
                is LodgingListViewModel.LodgingListStateUI.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
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
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No hay alojamientos registrados..", color = Color.Gray)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
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
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("⚠️ Error", fontWeight = FontWeight.Bold, color = Color.Red)
                            Text(message, color = Color.DarkGray)
                            Spacer(Modifier.height(16.dp))
                            Button(
                                onClick = { vm.load(id = userId ?: 0L) },
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
}

@Composable
private fun LodgingCard(lodging: Lodging, onDetails: (Long) -> Unit) {
    // 🔍 Precio dinámico: habitación simple o primer precio
    Log.d("LodgingCard", "Calculating price for lodging: ${lodging}")
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
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp))
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
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020)),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text("Ver", color = Color.White)
            }
        }
    }
}

// ★ NUEVO: Composable del banner (simple y sin dependencias extra)
@Composable
private fun AdBanner(
    imageUrl: String,
    onClose: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(Modifier.fillMaxWidth()) {

            // ✅ Usa AsyncImage (más simple y idiomático)
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Anuncio",
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            // Botón de cierre "X"
            TextButton(
                onClick = onClose,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                colors = ButtonDefaults.textButtonColors(containerColor = Color(0xAA000000))
            ) {
                Text("✕", color = Color.White, fontSize = 14.sp)
            }
        }
    }
}
