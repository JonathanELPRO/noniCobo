package com.calyrsoft.ucbp1.features.lodging.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
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
import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging

@Composable
fun LodgingListContent(
    lodgings: List<Lodging>,
    isFilterActive: Boolean,
    onClearFilters: () -> Unit,
    onDetails: (Long) -> Unit,
    onEdit: (Lodging) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {

        Surface(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFFFFEBEE)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(Icons.Default.Home, contentDescription = null, tint = Color(0xFFB00020), modifier = Modifier.size(16.dp))
                Text("${lodgings.size} ${if (lodgings.size == 1) "alojamiento" else "alojamientos"}",
                    fontSize = 13.sp, color = Color(0xFFB00020), fontWeight = FontWeight.Medium)
                if (isFilterActive) {
                    Spacer(Modifier.weight(1f))
                    Surface(onClick = onClearFilters, shape = CircleShape, color = Color(0xFFB00020)) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("Limpiar", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Medium)
                            Icon(Icons.Default.Close, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(lodgings, key = { it.id ?: 0L }) { lodging ->
                LodgingCard(lodging, onDetails,onEdit)
            }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}