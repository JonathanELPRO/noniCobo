package com.calyrsoft.ucbp1.features.payments.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.calyrsoft.ucbp1.features.payments.domain.model.PaymentModel
import org.koin.androidx.compose.getViewModel
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.Image

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    model: PaymentModel,
    onBack: () -> Unit = {}
) {
    val vm: PaymentViewModel = getViewModel()
    val state by vm.state.collectAsState()

    val scroll = rememberScrollState()
    val snackHost = remember { SnackbarHostState() }

    LaunchedEffect(model) {
        vm.loadPayment(model)
    }

    Scaffold(
        containerColor = Color(0xFFFDFDFD),
        snackbarHost = { SnackbarHost(snackHost) }
    ) { padding ->
        when (val st = state) {
            is PaymentViewModel.PaymentStateUI.Init,
            is PaymentViewModel.PaymentStateUI.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFB00020))
                }
            }

            is PaymentViewModel.PaymentStateUI.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = Color.Red)
                        Text(st.message, color = Color.Red)
                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = { vm.loadPayment(model) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020))
                        ) {
                            Text("Reintentar", color = Color.White)
                        }
                    }
                }
            }

            is PaymentViewModel.PaymentStateUI.Success -> {
                val payment = st.payment

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(scroll)
                ) {
                    // Encabezado
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(
                                Color(0xFFB00020),
                                shape = RoundedCornerShape(bottomStart = 80.dp, bottomEnd = 80.dp)
                            )
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.TopStart)
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                        }

                        Icon(
                            imageVector = Icons.Default.Money,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier
                                .size(80.dp)
                                .align(Alignment.Center)
                        )
                    }

                    // Contenido principal
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Resumen del pago", fontSize = 22.sp, color = Color.Black)
                        Divider()

                        InfoRow(Icons.Default.Person, "Usuario: ${payment.userName}")
                        InfoRow(Icons.Default.Hotel, "Alojamiento: ${payment.lodgingName}")
                        InfoRow(Icons.Default.Room, "Habitación: ${payment.selectedRoom?.name ?: "No seleccionada"}")
                        InfoRow(Icons.Default.Info, "Tipo de estadía: ${payment.selectedStay?.name ?: "No seleccionada"}")

                        if (payment.selectedStay?.name?.contains("HORA") == true && !payment.hours.isNullOrEmpty()) {
                            InfoRow(Icons.Default.Schedule, "Duración: ${payment.hours} Hr")
                        }

                        InfoRow(
                            Icons.Default.Schedule,
                            "Hora de llegada: ${if (payment.arrivalTime.isNotEmpty()) payment.arrivalTime else "No especificada"}"
                        )

                        Divider(Modifier.padding(vertical = 8.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.dp, Color(0xFFB00020), RoundedCornerShape(16.dp))
                                .padding(16.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Monto total", fontSize = 18.sp)
                                Text(
                                    "Bs. ${"%.2f".format(payment.selectedPrice)}",
                                    color = Color(0xFFB00020),
                                    fontSize = 24.sp
                                )
                            }
                        }




                        Spacer(Modifier.height(16.dp))

                        Button(
                            onClick = { vm.sendPayment(payment) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Proceder al pago (WhatsApp)", color = Color.White, fontSize = 18.sp)
                        }

                        // Feedback
                        LaunchedEffect(state) {
                            if (state is PaymentViewModel.PaymentStateUI.Sent) {
                                snackHost.showSnackbar("Abriendo WhatsApp…")
                            }
                        }
                    }
                }
            }

            is PaymentViewModel.PaymentStateUI.Sent -> {
                // Ya se maneja con snackbar, pero puedes dejar un loader
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFB00020))
                }
            }
        }
    }
}

@Composable
private fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Color(0xFFB00020))
        Spacer(Modifier.width(8.dp))
        Text(text, fontSize = 16.sp)
    }
}
