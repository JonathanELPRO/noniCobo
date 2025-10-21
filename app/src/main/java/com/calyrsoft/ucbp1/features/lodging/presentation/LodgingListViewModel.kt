package com.calyrsoft.ucbp1.features.lodging.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.GetAddinRealTime
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.GetAllLodgingsByAdminUseCase
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.GetAllLodgingsFromSupaBaseUseCase
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.ObserveAllLocalLodgingsUseCase
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.UpsertLodgingUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class LodgingListViewModel(
    private val getAllLodgingsByAdminUseCase: GetAllLodgingsByAdminUseCase,
    private val getAllLodgingsUseCase: GetAllLodgingsFromSupaBaseUseCase,
    private val upsertLodgingUseCase: UpsertLodgingUseCase,
    private val observeAllLocalLodgingsUseCase: ObserveAllLocalLodgingsUseCase,
    private val getAddinRealTime: GetAddinRealTime

) : ViewModel() {

    sealed class LodgingListStateUI {
        object Init : LodgingListStateUI()
        object Loading : LodgingListStateUI()
        data class Success(val lodgings: List<Lodging>) : LodgingListStateUI()
        data class Error(val message: String) : LodgingListStateUI()
    }

    private val _state = MutableStateFlow<LodgingListStateUI>(LodgingListStateUI.Init)
    val state: StateFlow<LodgingListStateUI> = _state.asStateFlow()

    private val _adUrl = MutableStateFlow<String?>(null)
    val adUrl: StateFlow<String?> = _adUrl.asStateFlow()

    private val _showAd = MutableStateFlow(true)
    val showAd: StateFlow<Boolean> = _showAd.asStateFlow()

    private var adStarted = false

    fun startAdListener() {
        if (adStarted) return
        adStarted = true
        viewModelScope.launch {
            getAddinRealTime()
                .catch { /* log/ignore */ }
                .collect { ad ->
                    _adUrl.value = ad.UrlImagen
                    // Si cambió la URL, fuerza que se muestre (opcional):
                    _showAd.value = true
                }
        }
    }

    fun dismissAd() { _showAd.value = false }
    fun resetAdVisibility() { _showAd.value = true }

    fun load(id:Long) {
        _state.value = LodgingListStateUI.Loading

        Log.d("LodgingListViewModel", "Loading lodgings for admin id: $id")
        viewModelScope.launch {
            val result=getAllLodgingsByAdminUseCase(id)

                result.catch { e ->
                    _state.value = LodgingListStateUI.Error(
                        e.message ?: "Error al obtener los alojamientos"
                    )
                }
                .collect { list ->
                    list.forEach { lodging ->
                        upsertLodgingUseCase(lodging)
                    }
                    _state.value = LodgingListStateUI.Success(list)

                }
        }
    }

    fun loadAll() {
        _state.value = LodgingListStateUI.Loading

        viewModelScope.launch {
            getAllLodgingsUseCase()
                .catch { e ->
                    observeLocalFallback()

                }
                .collect { list ->
                    list.forEach { lodging ->
                        upsertLodgingUseCase(lodging)
                    }
                    _state.value = LodgingListStateUI.Success(list)
                }
        }


    }

    private suspend fun observeLocalFallback() {
        observeAllLocalLodgingsUseCase()
            .catch { e ->
                _state.value = LodgingListStateUI.Error(
                    e.message ?: "Error al cargar los datos locales"
                )
            }
            .collect { localList ->
                if (localList.isNotEmpty()) {
                    Log.d("LodgingListViewModel", "Datos locales recuperados (${localList.size})")
                    _state.value = LodgingListStateUI.Success(localList)
                } else {
                    _state.value = LodgingListStateUI.Error("Sin conexión y sin datos locales disponibles")
                }
            }
    }
}
