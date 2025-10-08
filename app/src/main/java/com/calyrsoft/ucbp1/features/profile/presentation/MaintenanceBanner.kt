package com.calyrsoft.ucbp1.features.profile.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.calyrsoft.ucbp1.features.remoteconfig.data.manager.RemoteConfigManager

@Composable
fun MaintenanceBanner() {
    val isMaintenance = remember { mutableStateOf(false) }
    val message = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        // 🔹 Sincroniza con Firebase y activa los valores nuevos
        val success = RemoteConfigManager.fetchAndActivate()
        Log.d("BannerDebug", "fetch success: $success")

        // 🔹 Lee los valores actualizados inmediatamente después
        isMaintenance.value = RemoteConfigManager.isMaintenance()
        message.value = RemoteConfigManager.getMessage()

        // 🔹 Luego empieza a escuchar cambios en vivo
        RemoteConfigManager.listenRealtime {
            isMaintenance.value = RemoteConfigManager.isMaintenance()
            message.value = RemoteConfigManager.getMessage()
        }
    }

    if (isMaintenance.value) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(Color.Yellow)
                .padding(16.dp)
        ) {
            Text(text = message.value, color = Color.Black)
        }
    }
}

