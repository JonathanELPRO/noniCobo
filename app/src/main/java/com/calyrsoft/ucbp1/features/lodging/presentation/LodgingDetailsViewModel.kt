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
    private val details: GetLodgingDetailsUseCase,                 // LOCAL
    private val getLodgingDetailsUseCase: GetLodgingDetailsFromSupbaseUseCase // REMOTO
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
            // 1️⃣ Intentar primero local para soportar offline
            val localResult = runCatching { details(id) }   // si tu use case ya devuelve Result, ajusta esto

            localResult.getOrNull()?.fold(
                onSuccess = { lodgingLocal ->
                    // Tenemos algo en local → lo mostramos ya
                    _state.value = LodgingDetailsStateUI.Success(lodgingLocal)

                    // 2️⃣ Intentar refrescar desde remoto (sin romper si falla)
                    launch {
                        try {
                            val remoteResult = getLodgingDetailsUseCase(id)
                            remoteResult.fold(
                                onSuccess = { lodgingRemote ->
                                    _state.value = LodgingDetailsStateUI.Success(lodgingRemote)
                                },
                                onFailure = {
                                    // ignoramos, ya mostramos local
                                }
                            )
                        } catch (_: Exception) {
                            // ignorar error remoto si ya tenemos local
                        }
                    }
                },
                onFailure = {
                    // No hay nada local → intentamos remoto
                    try {
                        val remoteResult = getLodgingDetailsUseCase(id)
                        remoteResult.fold(
                            onSuccess = { lodging ->
                                _state.value = LodgingDetailsStateUI.Success(lodging)
                            },
                            onFailure = { error ->
                                _state.value = LodgingDetailsStateUI.Error(
                                    error.message ?: "Error al obtener los detalles del alojamiento"
                                )
                            }
                        )
                    } catch (e: Exception) {
                        _state.value = LodgingDetailsStateUI.Error(
                            "Sin conexión y sin datos locales disponibles"
                        )
                    }
                }
            )
        }
    }
}

