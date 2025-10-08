package com.calyrsoft.ucbp1.features.lodging.presentation

import androidx.lifecycle.ViewModel
import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.GetLodgingDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class LodgingDetailsViewModel(private val details: GetLodgingDetailsUseCase) : ViewModel() {
    private val _lodging = MutableStateFlow<Lodging?>(null)
    val lodging: StateFlow<Lodging?> = _lodging

    suspend fun load(id: Long) {
        _lodging.update { details(id) }
    }
}
