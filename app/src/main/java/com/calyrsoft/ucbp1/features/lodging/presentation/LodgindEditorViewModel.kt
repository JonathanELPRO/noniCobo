package com.calyrsoft.ucbp1.features.lodging.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calyrsoft.ucbp1.features.auth.domain.model.Role
import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.AddLodgingUseCase
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.EditLodgingUseCase
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.UpsertLodgingUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LodgingEditorViewModel(
    private val upsertLodgingUseCase: UpsertLodgingUseCase,
    private val addLodging: AddLodgingUseCase,
    private val editLodgingUseCase: EditLodgingUseCase
) : ViewModel() {

    sealed class LodgingEditorStateUI {
        object Init : LodgingEditorStateUI()
        object Updating : LodgingEditorStateUI()
        object UpdateSuccess : LodgingEditorStateUI()
        class UpdateError(val message: String) : LodgingEditorStateUI()
    }

    private val _state = MutableStateFlow<LodgingEditorStateUI>(LodgingEditorStateUI.Init)
    val state: StateFlow<LodgingEditorStateUI> = _state.asStateFlow()

    fun save(role: Role, lodging: Lodging) {
        _state.value = LodgingEditorStateUI.Updating
        Log.d("LodgingEditorViewModel", "Editing lodging with id: ${lodging.id}")
        Log.d("LodgingEditorViewModel", "name: ${lodging.name}")
        viewModelScope.launch {
            val result = if (lodging.id != null) {
                editLodgingUseCase(role, lodging)

            } else {
                addLodging(role, lodging)
            }

            result.fold(
                onSuccess = { _state.value = LodgingEditorStateUI.UpdateSuccess },
                onFailure = { error ->
                    _state.value = LodgingEditorStateUI.UpdateError(
                        error.message ?: "Error guardando alojamiento"
                    )
                }
            )
        }
    }
}
