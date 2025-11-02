package com.calyrsoft.ucbp1.features.lodging.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDialog(
    initial: LodgingListViewModel.FilterOptions = LodgingListViewModel.FilterOptions(),
    availableAddresses: Set<String> = emptySet(),
    onDismiss: () -> Unit,
    onApply: (LodgingListViewModel.FilterOptions) -> Unit
) {
    var selectedType by remember { mutableStateOf(initial.type) }
    var selectedAddress by remember { mutableStateOf(initial.address) }
    var selectedAvailability by remember { mutableStateOf(initial.availability) }
    var addressExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(42.dp),
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .wrapContentHeight(),
        title = {
            Text(
                "Filtros",
                fontSize = 22.sp,
                color = Color(0xFF1C1C1C),
                modifier = Modifier.padding(bottom = 4.dp)
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(22.dp),
                modifier = Modifier.fillMaxWidth()
            ) {

                // Tipo
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Tipo", fontSize = 15.sp, color = Color(0xFF6B6B6B))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterInnerContent(
                                label = "Todos",
                                icon = Icons.Default.Home,
                                selected = selectedType == LodgingListViewModel.FilterType.ALL,
                                modifier = Modifier.weight(1f),
                                onClick = { selectedType = LodgingListViewModel.FilterType.ALL }
                            )
                            FilterInnerContent(
                                label = "Residencial",
                                icon = Icons.Default.House,
                                selected = selectedType == LodgingListViewModel.FilterType.RESIDENCIAL,
                                modifier = Modifier.weight(1f),
                                onClick = { selectedType = LodgingListViewModel.FilterType.RESIDENCIAL }
                            )
                        }
                        FilterInnerContent(
                            label = "Motel",
                            icon = Icons.Default.Hotel,
                            selected = selectedType == LodgingListViewModel.FilterType.MOTEL,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { selectedType = LodgingListViewModel.FilterType.MOTEL }
                        )
                    }
                }

                // Disponibilidad
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Disponibilidad", fontSize = 15.sp, color = Color(0xFF6B6B6B))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterInnerContent(
                            label = "Todos",
                            icon = Icons.Default.Schedule,
                            selected = selectedAvailability == LodgingListViewModel.AvailabilityType.ALL,
                            modifier = Modifier.weight(1.2f),
                            onClick = { selectedAvailability = LodgingListViewModel.AvailabilityType.ALL }
                        )
                        FilterInnerContent(
                            label = "24h",
                            icon = Icons.Default.AccessTime,
                            selected = selectedAvailability == LodgingListViewModel.AvailabilityType.TRUE,
                            modifier = Modifier.weight(1f),
                            onClick = { selectedAvailability = LodgingListViewModel.AvailabilityType.TRUE }
                        )
                        FilterInnerContent(
                            label = "No 24h",
                            icon = Icons.Default.AccessTime,
                            selected = selectedAvailability == LodgingListViewModel.AvailabilityType.FALSE,
                            modifier = Modifier.weight(1f),
                            onClick = { selectedAvailability = LodgingListViewModel.AvailabilityType.FALSE }
                        )
                    }
                }

                // Dirección
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Dirección", fontSize = 15.sp, color = Color(0xFF6B6B6B))

                    ExposedDropdownMenuBox(
                        expanded = addressExpanded,
                        onExpandedChange = { addressExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = if (selectedAddress.isBlank()) "Todas" else selectedAddress,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Seleccionar dirección") },
                            leadingIcon = { Icon(Icons.Default.LocationOn, null) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = addressExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(18.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFE53935),
                                focusedLabelColor = Color(0xFFE53935),
                                focusedLeadingIconColor = Color(0xFFE53935)
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = addressExpanded,
                            onDismissRequest = { addressExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Todas") },
                                onClick = {
                                    selectedAddress = ""
                                    addressExpanded = false
                                },
                                leadingIcon = { Icon(Icons.Default.Home, null) }
                            )

                            availableAddresses.sorted().forEach { address ->
                                DropdownMenuItem(
                                    text = { Text(address) },
                                    onClick = {
                                        selectedAddress = address
                                        addressExpanded = false
                                    },
                                    leadingIcon = { Icon(Icons.Default.LocationOn, null) }
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Cancelar", fontSize = 15.sp, color = Color(0xFF777777))
                }
                TextButton(
                    onClick = {
                        onApply(
                            LodgingListViewModel.FilterOptions(
                                selectedType,
                                selectedAddress,
                                selectedAvailability
                            )
                        )
                    }
                ) {
                    Text("Aplicar", fontSize = 16.sp, color = Color(0xFFE53935))
                }
            }
        }
    )
}