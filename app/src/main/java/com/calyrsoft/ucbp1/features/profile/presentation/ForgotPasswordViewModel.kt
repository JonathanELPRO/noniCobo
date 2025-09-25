package com.calyrsoft.ucbp1.features.profile.presentation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calyrsoft.ucbp1.features.profile.domain.model.LoginUserModel
import com.calyrsoft.ucbp1.features.profile.domain.model.Name
import com.calyrsoft.ucbp1.features.profile.domain.model.Password
import com.calyrsoft.ucbp1.features.profile.domain.usecase.FindByNameUseCase
import com.calyrsoft.ucbp1.features.profile.domain.usecase.UpdateUserProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val findByNameUseCase: FindByNameUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase
) : ViewModel() {

    sealed class ForgotPasswordStateUI {
        object Init : ForgotPasswordStateUI()
        object Loading : ForgotPasswordStateUI()
        object Updating : ForgotPasswordStateUI()
        data class UserFound(val user: LoginUserModel) : ForgotPasswordStateUI()
        class UserNotFound(val message: String) : ForgotPasswordStateUI()
        data class UpdateSuccess(val user: LoginUserModel) : ForgotPasswordStateUI()
        class UpdateError(val message: String) : ForgotPasswordStateUI()
    }

    private val _state = MutableStateFlow<ForgotPasswordStateUI>(ForgotPasswordStateUI.Init)
    val state: StateFlow<ForgotPasswordStateUI> = _state.asStateFlow()

    /**
     * Verifica si el usuario existe.
     * Si se encuentra, se emitirá UserFound para mostrar el campo de nueva contraseña.
     */
    fun checkUserExists(username: String) {
        _state.value = ForgotPasswordStateUI.Loading

        viewModelScope.launch {
            val result = findByNameUseCase(Name.create(username).value)
            result.fold(
                onSuccess = { user ->
                    _state.value = ForgotPasswordStateUI.UserFound(user)
                },
                onFailure = {
                    _state.value = ForgotPasswordStateUI.UserNotFound("Usuario no encontrado")
                }
            )
        }
    }

    fun updatePassword(username: String, newPassword: String) {
        _state.value = ForgotPasswordStateUI.Updating

        viewModelScope.launch {
            val result = updateUserProfileUseCase(
                name = username,
                newPassword = Password.create(newPassword).value
            )
            result.fold(
                onSuccess = { updatedUser ->
                    _state.value = ForgotPasswordStateUI.UpdateSuccess(updatedUser)
                },
                onFailure = { error ->
                    _state.value =
                        ForgotPasswordStateUI.UpdateError(error.message ?: "Error al actualizar contraseña")
                }
            )
        }
    }

}
