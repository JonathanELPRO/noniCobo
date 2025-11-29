package com.calyrsoft.ucbp1.features.lodging.presentation

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calyrsoft.ucbp1.features.auth.domain.model.Role
import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.AddLodgingUseCase
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.EditLodgingUseCase
import com.calyrsoft.ucbp1.features.lodging.domain.usecase.UpsertLodgingUseCase
import com.calyrsoft.ucbp1.features.supabase.SupabaseStorageDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class LodgingEditorViewModel(
    private val upsertLodgingUseCase: UpsertLodgingUseCase,
    private val addLodging: AddLodgingUseCase,
    private val editLodgingUseCase: EditLodgingUseCase,
    private val storageDataSource: SupabaseStorageDataSource,
    private val appContext: Context
) : ViewModel() {

    sealed class LodgingEditorStateUI {
        object Init : LodgingEditorStateUI()
        object Updating : LodgingEditorStateUI()
        object UpdateSuccess : LodgingEditorStateUI()
        class UpdateError(val message: String) : LodgingEditorStateUI()
    }

    private val _state = MutableStateFlow<LodgingEditorStateUI>(LodgingEditorStateUI.Init)
    val state: StateFlow<LodgingEditorStateUI> = _state.asStateFlow()

    private suspend fun uploadImageIfNeeded(localUri: String?, imageType: String): Result<String?> {
        if (localUri == null) return Result.success(null)
        // Si ya es una URL remota, no subimos nada.
        if (localUri.startsWith("http")) return Result.success(localUri)

        return try {
            val uri = Uri.parse(localUri)
            val inputStream = appContext.contentResolver.openInputStream(uri)
                ?: return Result.failure(Exception("No se pudo abrir el archivo de imagen"))

            // Leemos la imagen directamente en memoria sin guardarla localmente
            val imageBytes = inputStream.use { it.readBytes() }

            // Generamos un nombre único para el archivo
            val filename = "${imageType}_${UUID.randomUUID()}.jpg"

            storageDataSource.uploadImageAndGetUrl(
                bucket = "TelosJoinBucket",
                imageBytes = imageBytes,
                filename = filename
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun save(role: Role, lodging: Lodging) {
        _state.value = LodgingEditorStateUI.Updating
        Log.d("LodgingEditorViewModel", "Editing lodging with id: ${lodging.id}")
        Log.d("LodgingEditorViewModel", "name: ${lodging.name}")
        viewModelScope.launch {
            // Subimos imágenes (si son locales) a Supabase y obtenemos las URLs.
            val placeImageResult = uploadImageIfNeeded(lodging.placeImageUri, "place")
            val licenseImageResult = uploadImageIfNeeded(lodging.licenseImageUri, "license")

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
