package com.calyrsoft.ucbp1.features.lodging.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LodgingListScreen(
    vm: LodgingListViewModel = koinViewModel(),
    onDetails: (Long) -> Unit
) {
    val lodgings by vm.lodgings.collectAsState()

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Alojamientos disponibles") }) }
    ) { innerPadding ->
        if (lodgings.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay alojamientos registrados.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(lodgings) { lodging ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(lodging.name, style = MaterialTheme.typography.titleMedium)
                            Text(lodging.type.name, style = MaterialTheme.typography.labelLarge)
                            lodging.district?.let {
                                Text("Zona: $it", style = MaterialTheme.typography.bodyMedium)
                            }
                            if (lodging.open24h) {
                                Text("Atiende 24h", style = MaterialTheme.typography.bodyMedium)
                            }

                            Spacer(Modifier.height(8.dp))
                            Button(
                                onClick = { lodging.id?.let(onDetails) },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("Ver detalle")
                            }
                        }
                    }
                }
            }
        }
    }
}
