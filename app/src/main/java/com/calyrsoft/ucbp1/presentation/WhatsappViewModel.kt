package com.calyrsoft.ucbp1.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calyrsoft.ucbp1.domain.usecase.GetFirstWhatsappNumberUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WhatsappViewModel(
    private val getFirstWhatsappNumberUseCase: GetFirstWhatsappNumberUseCase
) : ViewModel() {

    sealed class WhatsappStateUI {
        object Init : WhatsappStateUI()
        object Loading : WhatsappStateUI()
        class Success(val number: String) : WhatsappStateUI()
        class Error(val message: String) : WhatsappStateUI()
    }

    private val _state = MutableStateFlow<WhatsappStateUI>(WhatsappStateUI.Init)
    val state: StateFlow<WhatsappStateUI> = _state.asStateFlow()

    fun openWhatsapp() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = WhatsappStateUI.Loading

            val result = getFirstWhatsappNumberUseCase.invoke()

            result.fold(
                onSuccess = { number ->
                    _state.value = WhatsappStateUI.Success(number)
                },
                onFailure = { error ->
                    _state.value = WhatsappStateUI.Error(
                        error.message ?: "Error leyendo los números de WhatsApp"
                    )
                }
            )
        }
    }
}
