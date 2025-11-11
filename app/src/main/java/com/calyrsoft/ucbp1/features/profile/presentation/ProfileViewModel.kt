package com.calyrsoft.ucbp1.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calyrsoft.ucbp1.features.auth.domain.model.User
import com.calyrsoft.ucbp1.features.auth.domain.usecase.GetCurrentUserByEmailUseCase
import com.calyrsoft.ucbp1.features.auth.presentation.LoginViewModel2.LoginUIState
import com.calyrsoft.ucbp1.features.lodging.presentation.LodgingEditorViewModel.LodgingEditorStateUI
import com.calyrsoft.ucbp1.features.profile.domain.usecase.UpdateUserPasswordUseCase
import com.calyrsoft.ucbp1.features.profile.domain.usecase.UpdateUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getCurrentUserByEmailUseCase: GetCurrentUserByEmailUseCase,
    private val updateUserCase: UpdateUserUseCase,
    private val updateUserPasswordUseCase: UpdateUserPasswordUseCase
) : ViewModel() {

    sealed class ProfileStateUI {
        object Init : ProfileStateUI()
        object Loading : ProfileStateUI()
        object Updating : ProfileStateUI()
        object UpdateSuccess : ProfileStateUI()
        class UpdateError(val message: String) : ProfileStateUI()
        data class DataLoaded(val user: User) : ProfileStateUI()
    }

    private val _state = MutableStateFlow<ProfileStateUI>(ProfileStateUI.Init)
    val state: StateFlow<ProfileStateUI> = _state.asStateFlow()

    suspend fun loadProfile(userEmail: String) {
        _state.value = ProfileStateUI.Loading
        val userResult = getCurrentUserByEmailUseCase(userEmail)
        viewModelScope.launch {
            userResult.fold(
                onSuccess = { user ->
                    _state.value = ProfileStateUI.DataLoaded(user)
                },
                onFailure = { error ->
                    _state.value = ProfileStateUI.UpdateError(
                        error.message ?: "Error al cargar el perfil"
                    )
                }
            )
        }
    }

    fun updateProfile(
        id:Long,
        newName: String ,
        newPhone: String,
    ) {
        _state.value = ProfileStateUI.Updating
        viewModelScope.launch {
            val result = updateUserCase(id,newName, newPhone)
            result.fold(
                onSuccess = {
                    _state.value = ProfileStateUI.UpdateSuccess
                            },
                onFailure = { error ->
                    _state.value = ProfileStateUI.UpdateError(
                        error.message ?: "Error al guardar alojamiento"
                    )
                }
            )
        }
    }

    fun updateUserPassword(
        newPassword: String,
        token:String
    ) {
        _state.value = ProfileStateUI.Updating
        viewModelScope.launch {
            val result = updateUserPasswordUseCase(newPassword,token)
            result.fold(
                onSuccess = {
                    _state.value = ProfileStateUI.UpdateSuccess
                },
                onFailure = { error ->
                    _state.value = ProfileStateUI.UpdateError(
                        error.message ?: "Error al guardar alojamiento"
                    )
                }
            )
        }
    }
}
