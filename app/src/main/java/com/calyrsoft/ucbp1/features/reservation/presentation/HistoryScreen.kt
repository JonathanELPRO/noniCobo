package com.calyrsoft.ucbp1.features.reservation.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    vm: ReservationViewModel = viewModel(),
    userId: Long
) {
    val uiState by vm.uiState.collectAsState()

    LaunchedEffect(userId) { vm.observeReservations(userId) }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Historial de Reservas") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            items(uiState.reservations) { r ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text("Estado: ${r.status}", style = MaterialTheme.typography.labelLarge)
                        Text("Tipo: ${r.stayType}")
                        Text("Total: Bs. ${r.total}")
                        Text("Pagado: Bs. ${r.advancePaid}")
                    }
                }
            }
        }
    }
}
