package com.calyrsoft.ucbp1.features.reservation.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.calyrsoft.ucbp1.features.reservation.domain.model.Reservation
import com.calyrsoft.ucbp1.features.reservation.domain.model.StayType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationScreen(
    vm: ReservationViewModel = viewModel(),
    userId: Long,
    lodgingId: Long,
    onCreated: () -> Unit
) {
    var stayType by remember { mutableStateOf(StayType.HOUR) }
    var hours by remember { mutableStateOf("1") }
    var total by remember { mutableStateOf("") }

    val uiState by vm.uiState.collectAsState()

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Nueva Reserva") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DropdownMenuDemo(stayType) { stayType = it }

            OutlinedTextField(value = hours, onValueChange = { hours = it }, label = { Text("Horas") })
            OutlinedTextField(value = total, onValueChange = { total = it }, label = { Text("Total (Bs)") })

            Button(
                onClick = {
                    val reservation = Reservation(
                        userId = userId,
                        lodgingId = lodgingId,
                        stayType = stayType,
                        hours = hours.toIntOrNull(),
                        startMillis = System.currentTimeMillis(),
                        endMillis = System.currentTimeMillis() + (hours.toIntOrNull() ?: 1) * 3600000L,
                        status = "CREATED",
                        total = total.toDoubleOrNull() ?: 0.0,
                        advancePaid = 0.0
                    )
                    vm.create(reservation)
                    onCreated()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar Reserva")
            }

            uiState.message?.let {
                Text(it, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
private fun DropdownMenuDemo(current: StayType, onSelect: (StayType) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Button(onClick = { expanded = true }) {
            Text(current.name)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            StayType.values().forEach { t ->
                DropdownMenuItem(
                    text = { Text(t.name) },
                    onClick = {
                        onSelect(t)
                        expanded = false
                    }
                )
            }
        }
    }
}
