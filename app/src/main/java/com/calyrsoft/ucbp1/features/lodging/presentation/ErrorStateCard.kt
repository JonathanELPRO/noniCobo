package com.calyrsoft.ucbp1.features.lodging.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ErrorStateCard(
    message: String,
    onRetry: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Card(modifier = Modifier.fillMaxWidth(0.9f).padding(24.dp), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(modifier = Modifier.padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Surface(modifier = Modifier.size(64.dp), shape = CircleShape, color = Color(0xFFFFEBEE)) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.ErrorOutline, contentDescription = null, modifier = Modifier.size(32.dp), tint = Color(0xFFB00020))
                    }
                }
                Text("Error al cargar", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                Text(message, fontSize = 14.sp, color = Color.Gray, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                Button(onClick = onRetry, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020)), shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(48.dp)) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Reintentar", fontSize = 15.sp)
                }
            }
        }
    }
}