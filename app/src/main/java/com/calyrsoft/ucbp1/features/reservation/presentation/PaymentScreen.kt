package com.calyrsoft.ucbp1.features.reservation.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    vm: ReservationViewModel = viewModel(),
    reservationId: Long
) {
    var amount by remember { mutableStateOf("") }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Registrar Pago") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Monto (Bs)") })
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    vm.payAdvance(reservationId, amount.toDoubleOrNull() ?: 0.0)
                }) {
                    Text("Pago Anticipo")
                }
                Button(onClick = {
                    vm.payRemaining(reservationId, amount.toDoubleOrNull() ?: 0.0)
                }) {
                    Text("Pago Restante")
                }
            }
        }
    }
}
