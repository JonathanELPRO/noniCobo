package com.calyrsoft.ucbp1.features.whatsapp.presentation

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import org.koin.androidx.compose.koinViewModel

@Composable
fun WhatsappButton(modifier: Modifier = Modifier) {
    val whatsappVM: WhatsappViewModel = koinViewModel()
    val whatsappState by whatsappVM.state.collectAsState()

    when(val ws = whatsappState) {
        is WhatsappViewModel.WhatsappStateUI.Loading -> Text("Abriendo WhatsApp...")
        is WhatsappViewModel.WhatsappStateUI.Success -> {
            val context = LocalContext.current
            val url = "https://wa.me/${ws.number}"
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            context.startActivity(intent)
        }
        is WhatsappViewModel.WhatsappStateUI.Error -> Text("Error: ${ws.message}", color = Color.Red)
        is WhatsappViewModel.WhatsappStateUI.Init -> {}
    }

    Button(
        onClick = { whatsappVM.openWhatsapp() },
        modifier = modifier.fillMaxWidth()
    ) {
        Text("Contactaaaaaa por WhatsApp")
    }
}
