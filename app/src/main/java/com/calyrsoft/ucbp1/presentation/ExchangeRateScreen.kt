package com.calyrsoft.ucbp1.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExchangeRateScreen(
    modifier: Modifier = Modifier,
    vm: ExchangeRateViewModel = koinViewModel()
) {
    var currency by remember { mutableStateOf("") }
    val state by vm.state.collectAsState()

    Column(modifier = modifier.padding(16.dp)) {

        OutlinedTextField(
            value = currency,
            onValueChange = { currency = it },
            label = { Text("Ingrese la moneda (USD, EUR, GBP...)") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { vm.fetchAlias(currency) },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Consultar")
        }

        when (val st = state) {
            is ExchangeRateViewModel.ExchangeRateUI.Init -> Text("Ingrese una moneda para consultar")
            is ExchangeRateViewModel.ExchangeRateUI.Loading -> Text("Cargando tasas de cambio...")
            is ExchangeRateViewModel.ExchangeRateUI.Error -> Text("Error: ${st.message}")
            is ExchangeRateViewModel.ExchangeRateUI.Success -> {
                val exchangeRate = st.exchangeRate
                Text("Base: ${exchangeRate.baseCurrency}")
                Text("Moneda: ${exchangeRate.currency}")
                Text("Compra: ${exchangeRate.buyRate} ${exchangeRate.currency}")
                Text("Venta: ${exchangeRate.sellRate} ${exchangeRate.currency}")
            }
        }
    }
}
