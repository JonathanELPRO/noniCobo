package com.calyrsoft.ucbp1.features.lodging.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.GetAllLodgingsByAdminUseCase
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.GetAllLodgingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class LodgingListViewModel(
    private val getAll: GetAllLodgingsUseCase,
    private val getAllLodgingsByAdminUseCase: GetAllLodgingsByAdminUseCase
) : ViewModel() {

    sealed class LodgingListStateUI {
        object Init : LodgingListStateUI()
        object Loading : LodgingListStateUI()
        data class Success(val lodgings: List<Lodging>) : LodgingListStateUI()
        data class Error(val message: String) : LodgingListStateUI()
    }

    private val _state = MutableStateFlow<LodgingListStateUI>(LodgingListStateUI.Init)
    val state: StateFlow<LodgingListStateUI> = _state.asStateFlow()

    fun load(id:Long) {
        _state.value = LodgingListStateUI.Loading

        Log.d("LodgingListViewModel", "Loading lodgings for admin id: $id")
        viewModelScope.launch {
            getAllLodgingsByAdminUseCase(id)
                .catch { e ->
                    _state.value = LodgingListStateUI.Error(
                        e.message ?: "Error al obtener los alojamientos"
                    )
                }
                .collect { list ->
                    _state.value = LodgingListStateUI.Success(list)
                }
        }
    }
}
