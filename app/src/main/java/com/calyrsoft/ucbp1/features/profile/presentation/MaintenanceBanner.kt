package com.calyrsoft.ucbp1.features.profile.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.calyrsoft.ucbp1.features.remoteconfig.data.manager.RemoteConfigManager

@Composable
fun MaintenanceBanner() {
    val isMaintenance = remember { mutableStateOf(false) }
    val message = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val success = RemoteConfigManager.fetchAndActivate()
        Log.d("BannerDebug", "fetch success: $success")

        isMaintenance.value = RemoteConfigManager.isMaintenance()
        message.value = RemoteConfigManager.getMessage()

        RemoteConfigManager.listenRealtime {
            isMaintenance.value = RemoteConfigManager.isMaintenance()
            message.value = RemoteConfigManager.getMessage()
        }
    }

    if (isMaintenance.value) {
        // 🔒 Cubre toda la pantalla y BLOQUEA cualquier interacción
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xCC000000))
                // 👇 intercepta todos los toques (bloquea clicks, scrolls, etc.)
                .pointerInput(Unit) { detectTapGestures { /* no-op */ } }
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                color = Color(0xFFFFF176), // Amarillo suave
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "🛠️ Modo Mantenimiento",
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = message.value.ifBlank { "El sistema está temporalmente en mantenimiento. Intente más tarde." },
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
