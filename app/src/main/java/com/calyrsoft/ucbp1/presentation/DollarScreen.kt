package com.calyrsoft.ucbp1.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
fun DollarScreen(viewModelDollar: DollarViewModel = koinViewModel()) {
    val state = viewModelDollar.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        when (val stateValue = state.value) {
            is DollarViewModel.DollarUIState.Init -> Text("Iniciando")

            is DollarViewModel.DollarUIState.Error -> Text(stateValue.message)

            is DollarViewModel.DollarUIState.Loading -> CircularProgressIndicator()

            is DollarViewModel.DollarUIState.Success -> {
                Text("📡 EN VIVO")
                Text("Oficial: ${stateValue.data.dollarOfficial}")
                Text("Paralelo: ${stateValue.data.dollarParallel}")

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModelDollar.getHistoryOfDollarsFromLocalDB() }
                ) {
                    Text("Ver historial")
                }
            }

            is DollarViewModel.DollarUIState.History -> {
                Text("Historial de dólares:")

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(stateValue.data) { item ->
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                "Oficial: ${item.dollarOfficial} | " +
                                        "Paralelo: ${item.dollarParallel} | " +
                                        "Timestamp: ${item.timestamp}"
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { viewModelDollar.deleteByTimestamp(item.timestamp) }
                            ) {
                                Text("Eliminar este registro")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModelDollar.getDollarFromFireBase() }
                ) {
                    Text("Volver a EN VIVO")
                }
            }
        }
    }
}
