package com.calyrsoft.ucbp1.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calyrsoft.ucbp1.features.profile.domain.model.Email
import com.calyrsoft.ucbp1.features.profile.domain.model.ImageUrl
import com.calyrsoft.ucbp1.features.profile.domain.model.LoginUserModel
import com.calyrsoft.ucbp1.features.profile.domain.model.Name
import com.calyrsoft.ucbp1.features.profile.domain.model.Password
import com.calyrsoft.ucbp1.features.profile.domain.model.Phone
import com.calyrsoft.ucbp1.features.profile.domain.model.Summary
import com.calyrsoft.ucbp1.features.profile.domain.usecase.FindByNameUseCase
import com.calyrsoft.ucbp1.features.profile.domain.usecase.GetUserNameUseCase
import com.calyrsoft.ucbp1.features.profile.domain.usecase.UpdateUserProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val findByNameUseCase: FindByNameUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val getUserNameUseCase: GetUserNameUseCase
) : ViewModel() {

    sealed class ProfileStateUI {
        object Init : ProfileStateUI()
        object Loading : ProfileStateUI()
        object Updating : ProfileStateUI()
        data class UpdateSuccess(val user: LoginUserModel) : ProfileStateUI()
        class UpdateError(val message: String) : ProfileStateUI()

        class DataLoaded(val user: LoginUserModel) : ProfileStateUI()
    }

    private val _state = MutableStateFlow<ProfileStateUI>(ProfileStateUI.Init)
    val state: StateFlow<ProfileStateUI> = _state.asStateFlow()

    fun loadProfile(userId: String? = null) {
        _state.value = ProfileStateUI.Loading

        viewModelScope.launch {
            val name = userId ?: getUserNameUseCase().getOrNull()
            if (name == null) {
                _state.value = ProfileStateUI.UpdateError("Usuario no encontrado")
                return@launch
            }

            val result = findByNameUseCase(Name.create(name).value)
            result.fold(
                onSuccess = { user -> _state.value = ProfileStateUI.DataLoaded(user) },
                onFailure = { error -> _state.value = ProfileStateUI.UpdateError(error.message ?: "Error al cargar perfil") }
            )
        }
    }

    fun updateProfile(
        name: String,
        newName: String? = null,
        newPhone: String? = null,
        newImageUrl: String? = null,
        newPassword: String? = null,
        newEmail: String? = null,
        newSummary: String? = null
    ) {
        _state.value = ProfileStateUI.Updating
        try {
            val result = updateUserProfileUseCase(
                name = Name.create(name).value,
                newName = newName?.let { Name.create(it).value },
                newPhone = newPhone?.let { Phone.create(it).value },
                newImageUrl = newImageUrl?.let { ImageUrl.create(it).value },
                newPassword = newPassword?.let { Password.create(it).value },
                newEmail = newEmail?.let { Email.create(it).value },
                newSummary = newSummary?.let { Summary.create(it).value }
            )

            result.fold(
                onSuccess = { updatedUser -> _state.value = ProfileStateUI.UpdateSuccess(updatedUser) },
                onFailure = { error -> _state.value = ProfileStateUI.UpdateError(error.message ?: "Error al actualizar perfil") }
            )
        } catch (e: Exception) {
            _state.value = ProfileStateUI.UpdateError(e.message ?: "Error de validación")
        }
    }


}
