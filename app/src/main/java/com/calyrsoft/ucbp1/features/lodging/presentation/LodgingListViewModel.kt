package com.calyrsoft.ucbp1.features.lodging.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.GetAddinRealTime
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.GetAllLodgingsByAdminUseCase
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.GetAllLodgingsFromSupaBaseUseCase
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.ObserveAllLocalLodgingsUseCase
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.SearchByNameAndAdminIdUseCase
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.SearchLodgingByNameUseCase
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
    private val getAddinRealTime: GetAddinRealTime,
    private val searchLodgingByName: SearchLodgingByNameUseCase,
    private val searchByNameAndAdminIdUseCase: SearchByNameAndAdminIdUseCase

) : ViewModel() {

    sealed class LodgingListStateUI {
        object Init : LodgingListStateUI()
        object Loading : LodgingListStateUI()
        data class Success(val lodgings: List<Lodging>) : LodgingListStateUI()
        data class Error(val message: String) : LodgingListStateUI()
    }

    data class FilterOptions(
        val type: FilterType = FilterType.ALL,
        val address: String = "",
        val availability: AvailabilityType = AvailabilityType.ALL
    )

    enum class FilterType { ALL, RESIDENCIAL, MOTEL }
    enum class AvailabilityType { ALL, TRUE, FALSE }



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
            val result = getAllLodgingsByAdminUseCase(id)

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
                    applyFilters() // --- NUEVO ---
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
                    applyFilters() // --- NUEVO ---
                }
        }
    }

    private suspend fun observeLocalFallback(name:String="") {
        observeAllLocalLodgingsUseCase()
            .catch { e ->
                _state.value = LodgingListStateUI.Error(
                    e.message ?: "Error al cargar los datos locales"
                )
            }
            .collect { localList ->
                if (localList.isNotEmpty()) {
                    Log.d("LodgingListViewModel", "Datos locales recuperados (${localList.size})")
                    if (name.isNotEmpty()) {
                        val filteredByName = localList.filter { lodging ->
                            lodging.name?.contains(name, true) == true
                        }
                        _state.value = LodgingListStateUI.Success(filteredByName)
                    } else _state.value = LodgingListStateUI.Success(localList)
                    applyFilters() // --- NUEVO ---
                } else {
                    _state.value = LodgingListStateUI.Error("Sin conexión y sin datos locales disponibles")
                }
            }
    }

    fun searchByName(name:String) {
        _state.value = LodgingListStateUI.Loading

        viewModelScope.launch {
            searchLodgingByName(name)
                .catch {
                    observeLocalFallback(name)
                }
                .collect { list ->
                    list.forEach { lodging ->
                        upsertLodgingUseCase(lodging)
                    }
                    _state.value = LodgingListStateUI.Success(list)
                    applyFilters()
                }
        }
    }

    fun searchByNameAndAdminId(name:String, adminId: Long) {
        _state.value = LodgingListStateUI.Loading

        viewModelScope.launch {
            searchByNameAndAdminIdUseCase(name, adminId)
                .catch {
                    observeLocalFallback()
                }
                .collect { list ->
                    list.forEach { lodging ->
                        upsertLodgingUseCase(lodging)
                    }
                    _state.value = LodgingListStateUI.Success(list)
                    applyFilters()
                }
        }
    }

//estados y filtros
    private val _filters = MutableStateFlow(FilterOptions())
    val filters: StateFlow<FilterOptions> = _filters.asStateFlow()

    private val _filteredList = MutableStateFlow<List<Lodging>>(emptyList())
    val filteredList: StateFlow<List<Lodging>> = _filteredList.asStateFlow()

    private val _isFilterActive = MutableStateFlow(false)
    val isFilterActive: StateFlow<Boolean> = _isFilterActive.asStateFlow()

    fun updateFilters(newFilters: FilterOptions) {
        _filters.value = newFilters
        applyFilters()
    }

    fun clearFilters() {
        _filters.value = FilterOptions()
        applyFilters()
    }

    private fun applyFilters() {

        val uiState = state.value
        if (uiState !is LodgingListStateUI.Success) {
            _filteredList.value = emptyList()
            return
        }

        val all = uiState.lodgings
        val filter = _filters.value

        val result = all.filter { lodging ->

            val typeOk = when (filter.type) {
                FilterType.ALL -> true
                FilterType.RESIDENCIAL -> lodging.type?.name.equals("Residencial", true)
                FilterType.MOTEL -> lodging.type?.name.equals("Motel", true)
            }

            val addressOk =
                filter.address.isBlank() ||
                        lodging.address?.contains(filter.address, true) == true

            val availabilityOk = when (filter.availability) {
                AvailabilityType.ALL -> true
                AvailabilityType.TRUE -> lodging.open24h
                AvailabilityType.FALSE -> !lodging.open24h
            }

            typeOk && addressOk && availabilityOk
        }

        _filteredList.value = result

        _isFilterActive.value =
            filter.type != FilterType.ALL ||
                    filter.address.isNotEmpty() ||
                    filter.availability != AvailabilityType.ALL
    }
}
