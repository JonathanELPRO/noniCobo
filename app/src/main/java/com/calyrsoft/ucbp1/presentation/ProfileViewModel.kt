package com.calyrsoft.ucbp1.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calyrsoft.ucbp1.domain.model.LoginUserModel
import com.calyrsoft.ucbp1.domain.usecase.FindByNameAndPasswordUseCase
import com.calyrsoft.ucbp1.domain.usecase.FindByNameUseCase
import com.calyrsoft.ucbp1.domain.usecase.UpdateUserProfileUseCase
import com.calyrsoft.ucbp1.presentation.LoginViewModel.LoginStateUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val findByNameUseCase: FindByNameUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase
) : ViewModel() {

    sealed class ProfileStateUI {
        object Init : ProfileStateUI()
        object Loading : ProfileStateUI() //
        object Updating : ProfileStateUI()
        data class UpdateSuccess(val user: LoginUserModel) : ProfileStateUI()
        class UpdateError(val message: String) : ProfileStateUI()//

        class DataLoaded(val user: LoginUserModel) : ProfileStateUI() //
    }

    private val _state = MutableStateFlow<ProfileStateUI>(ProfileStateUI.Init)
    val state: StateFlow<ProfileStateUI> = _state.asStateFlow()

    fun loadProfile(userId: String) {

            _state.value = ProfileStateUI.Loading

            val result = findByNameUseCase.invoke(userId)
            result.fold(
                onSuccess = { user ->
                    _state.value = ProfileStateUI.DataLoaded(user)
                },

                onFailure = { error ->
                    _state.value = ProfileStateUI.UpdateError(error.message ?: "Error al cargar perfil")
                }
            )

    }

    fun updateProfile(name: String,
                      newName: String? = null,
                      newPhone: String? = null,
                      newImageUrl: String? = null,
                      newPassword: String? = null) {

            _state.value = ProfileStateUI.Updating

            val result = updateUserProfileUseCase.invoke(name, newName, newPhone, newImageUrl, newPassword)

            result.fold(
                onSuccess = { updatedUser ->
                    _state.value = ProfileStateUI.UpdateSuccess(updatedUser)
                },
                onFailure = { error ->
                    _state.value =
                        ProfileStateUI.UpdateError(error.message ?: "Error al actualizar nombre")
                }
            )

    }

    // Puedes crear funciones similares para actualizar phone, password, imageUrl, etc.
}
