package com.calyrsoft.ucbp1.features.lodging.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.GetLodgingDetailsFromSupbaseUseCase
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.GetLodgingDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LodgingDetailsViewModel(
    private val details: GetLodgingDetailsUseCase,
    private val getLodgingDetailsUseCase: GetLodgingDetailsFromSupbaseUseCase
) : ViewModel() {

    sealed class LodgingDetailsStateUI {
        object Init : LodgingDetailsStateUI()
        object Loading : LodgingDetailsStateUI()
        data class Success(val lodging: Lodging) : LodgingDetailsStateUI()
        data class Error(val message: String) : LodgingDetailsStateUI()
    }

    private val _state = MutableStateFlow<LodgingDetailsStateUI>(LodgingDetailsStateUI.Init)
    val state: StateFlow<LodgingDetailsStateUI> = _state.asStateFlow()

    fun load(id: Long) {
        _state.value = LodgingDetailsStateUI.Loading

        viewModelScope.launch {
            val result = getLodgingDetailsUseCase(id)
            result.fold(
                onSuccess = { lodging ->
                    _state.value = LodgingDetailsStateUI.Success(lodging)
                },
                onFailure = { error ->
                    _state.value = LodgingDetailsStateUI.Error(
                        error.message ?: "Error al obtener los detalles del alojamiento"
                    )
                }
            )
        }
    }
}
