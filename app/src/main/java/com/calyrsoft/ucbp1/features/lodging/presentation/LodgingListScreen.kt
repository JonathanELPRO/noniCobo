package com.calyrsoft.ucbp1.features.lodging.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier


//@Composable
//fun LodgingListScreen(vm: LodgingListViewModel, onDetails: (Long) -> Unit) {
//    val lodgingsList by vm.lodgings.collectAsState()
//    LazyColumn(modifier = Modifier.fillMaxSize()) {
//        items(lodgingsList) { l ->
//            val id = l.id
//            ListItem(
//                headlineText = { Text(l.name) },
//                supportingText = { Text("${l.type} · ${l.district ?: ""}") },
//                trailingContent = {
//                    val price = l.pricePerHour ?: l.pricePerNight ?: l.pricePerDay
//                    Text(price?.toInt()?.toString() ?: "")
//                },
//                modifier = if (id != null) Modifier.clickable { onDetails(id) } else Modifier
//            )
//            Divider()
//        }
//    }
//}