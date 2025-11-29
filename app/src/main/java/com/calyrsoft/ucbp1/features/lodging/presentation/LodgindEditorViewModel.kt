package com.calyrsoft.ucbp1.features.lodging.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calyrsoft.ucbp1.features.auth.domain.model.Role
import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.AddLodgingUseCase
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.EditLodgingUseCase
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.UploadImageToSupabaseUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LodgingEditorViewModel(
    private val addLodging: AddLodgingUseCase,
    private val editLodgingUseCase: EditLodgingUseCase,
    private val uploadImageToSupabaseUseCase: UploadImageToSupabaseUseCase
) : ViewModel() {

    sealed class LodgingEditorStateUI {
        object Init : LodgingEditorStateUI()
        object Updating : LodgingEditorStateUI()
        object UpdateSuccess : LodgingEditorStateUI()
        data class UpdateError(val message: String) : LodgingEditorStateUI()
    }

    private val _state = MutableStateFlow<LodgingEditorStateUI>(LodgingEditorStateUI.Init)
    val state: StateFlow<LodgingEditorStateUI> = _state.asStateFlow()

    fun save(role: Role, lodging: Lodging) {
        _state.value = LodgingEditorStateUI.Updating
        Log.d("LodgingEditorViewModel", "Guardando lodging con id: ${lodging.id}, nombre: ${lodging.name}")
        
        viewModelScope.launch {
            // Subimos imágenes (si son locales) a Supabase y obtenemos las URLs
            val placeImageResult = uploadImageToSupabaseUseCase(lodging.placeImageUri, "place")
            val licenseImageResult = uploadImageToSupabaseUseCase(lodging.licenseImageUri, "license")

            val placeImageUrl = placeImageResult.getOrElse { error ->
                _state.value = LodgingEditorStateUI.UpdateError(
                    "Error subiendo imagen del local: ${error.message}"
                )
                return@launch
            }

            val licenseImageUrl = licenseImageResult.getOrElse { error ->
                _state.value = LodgingEditorStateUI.UpdateError(
                    "Error subiendo imagen de licencia: ${error.message}"
                )
                return@launch
            }

            val lodgingWithRemoteImages = lodging.copy(
                placeImageUri = placeImageUrl,
                licenseImageUri = licenseImageUrl
            )

            Log.d("LodgingEditorViewModel", "URLs de imágenes a guardar:")
            Log.d("LodgingEditorViewModel", "placeImageUri: $placeImageUrl")
            Log.d("LodgingEditorViewModel", "licenseImageUri: $licenseImageUrl")

            val result = if (lodgingWithRemoteImages.id != null) {
                editLodgingUseCase(role, lodgingWithRemoteImages)
            } else {
                addLodging(role, lodgingWithRemoteImages)
            }

            result.fold(
                onSuccess = { 
                    _state.value = LodgingEditorStateUI.UpdateSuccess 
                },
                onFailure = { error ->
                    _state.value = LodgingEditorStateUI.UpdateError(
                        error.message ?: "Error guardando alojamiento"
                    )
                }
            )
        }
    }
}
