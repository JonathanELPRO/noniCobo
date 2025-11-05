package com.calyrsoft.ucbp1.features.payments.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calyrsoft.ucbp1.features.payments.domain.model.PaymentModel
import com.calyrsoft.ucbp1.features.payments.domain.usecase.SendPaymentWhatsAppUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PaymentViewModel(
    private val sendPaymentWhatsAppUseCase: SendPaymentWhatsAppUseCase // 👈 inyección del caso de uso
) : ViewModel() {

    sealed class PaymentStateUI {
        object Init : PaymentStateUI()
        object Loading : PaymentStateUI()
        data class Success(val payment: PaymentModel) : PaymentStateUI()
        data class Sent(val message: String) : PaymentStateUI()
        data class Error(val message: String) : PaymentStateUI()
    }

    private val _state = MutableStateFlow<PaymentStateUI>(PaymentStateUI.Init)
    val state: StateFlow<PaymentStateUI> = _state.asStateFlow()

    // 🔹 Cargar modelo (lo mantienes igual)
    fun loadPayment(model: PaymentModel) {
        _state.value = PaymentStateUI.Loading

        viewModelScope.launch {
            try {
                _state.value = PaymentStateUI.Success(model)
            } catch (e: Exception) {
                _state.value = PaymentStateUI.Error(e.message ?: "Error al cargar el pago")
            }
        }
    }

    // 🔹 Nuevo método para enviar mensaje por WhatsApp
    fun sendPayment(model: PaymentModel) {
        _state.value = PaymentStateUI.Loading

        viewModelScope.launch {
            val result = sendPaymentWhatsAppUseCase(model)
            result.fold(
                onSuccess = {
                    _state.value = PaymentStateUI.Sent("Mensaje enviado correctamente por WhatsApp")
                },
                onFailure = { e ->
                    _state.value = PaymentStateUI.Error(e.message ?: "Error al abrir WhatsApp")
                }
            )
        }
    }
}
