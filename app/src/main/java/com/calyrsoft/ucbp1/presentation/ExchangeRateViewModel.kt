package com.calyrsoft.ucbp1.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calyrsoft.ucbp1.domain.model.ExchangeRateModel
import com.calyrsoft.ucbp1.domain.usecase.GetExchangeRateUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExchangeRateViewModel(
    val useCase: GetExchangeRateUseCase
): ViewModel()  {
    sealed class ExchangeRateUI {
        object Init: ExchangeRateUI()
        object Loading: ExchangeRateUI()
        class Error(val message: String) : ExchangeRateUI()
        class Success(val exchangeRate: ExchangeRateModel) : ExchangeRateUI()

    }

    private val _state = MutableStateFlow<ExchangeRateViewModel.ExchangeRateUI>(ExchangeRateViewModel.ExchangeRateUI.Init)

    val state: StateFlow<ExchangeRateViewModel.ExchangeRateUI> = _state.asStateFlow()

    fun fetchAlias(currency: String) {
        viewModelScope.launch(Dispatchers.IO) {


            _state.value = ExchangeRateUI.Loading

            val result = useCase.invoke(currency)

            result.fold(
                onSuccess = { exchangeRate ->
                    _state.value = ExchangeRateUI.Success(exchangeRate)
                },

                onFailure = { error ->
                    _state.value = ExchangeRateUI.Error(message = error.message ?: "Error desconocido")
                }
            )


        }
    }

}