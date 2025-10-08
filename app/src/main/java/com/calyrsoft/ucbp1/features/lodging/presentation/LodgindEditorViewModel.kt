package com.calyrsoft.ucbp1.features.lodging.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calyrsoft.ucbp1.features.auth.domain.model.Role
import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.UpsertLodgingUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LodgingEditorUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val success: Boolean = false
)

class LodgingEditorViewModel(
    private val upsertLodging: UpsertLodgingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LodgingEditorUiState())
    val uiState: StateFlow<LodgingEditorUiState> = _uiState

    fun save(role: Role, lodging: Lodging) {
        viewModelScope.launch {
            _uiState.value = LodgingEditorUiState(isLoading = true)
            try {
                upsertLodging(role, lodging)
                _uiState.value = LodgingEditorUiState(success = true)
            } catch (e: Exception) {
                _uiState.value = LodgingEditorUiState(errorMessage = e.message)
            }
        }
    }
}
