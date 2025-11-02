package com.calyrsoft.ucbp1.features.lodging.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
fun EmptyState(
    isFilterActive: Boolean,
    onClearFilters: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Surface(modifier = Modifier.size(80.dp), shape = CircleShape, color = Color(0xFFF5F5F5)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.SearchOff, contentDescription = null, modifier = Modifier.size(40.dp), tint = Color.Gray.copy(alpha = 0.5f))
                }
            }
            Text("No se encontraron alojamientos", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF424242))
            Text(if (isFilterActive) "Intenta ajustar los filtros" else "No hay alojamientos registrados", fontSize = 14.sp, color = Color.Gray)
            if (isFilterActive) {
                Spacer(Modifier.height(4.dp))
                Button(onClick = onClearFilters, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020)), shape = RoundedCornerShape(12.dp)) {
                    Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Limpiar filtros")
                }
            }
        }
    }
}
