package com.calyrsoft.ucbp1.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calyrsoft.ucbp1.features.profile.domain.model.LoginUserModel
import com.calyrsoft.ucbp1.features.profile.domain.model.Name
import com.calyrsoft.ucbp1.features.profile.domain.model.Password
import com.calyrsoft.ucbp1.features.profile.domain.usecase.FindByNameAndPasswordUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    val useCase: FindByNameAndPasswordUseCase): ViewModel()  {
    sealed class LoginStateUI {
        object Init: LoginStateUI()
        object Loading: LoginStateUI()
        class Error(val message: String) : LoginStateUI()
        class Success(val loginUser: LoginUserModel) : LoginStateUI()

    }

    private val _state = MutableStateFlow<LoginStateUI>(LoginStateUI.Init)

    val state: StateFlow<LoginStateUI> = _state.asStateFlow()

    fun fetchAlias(name: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = LoginStateUI.Loading

            try {
                val result = useCase(
                    Name.create(name).value,
                    Password.create(password).value
                )

                result.fold(
                    onSuccess = { user -> _state.value = LoginStateUI.Success(user) },
                    onFailure = { error -> _state.value = LoginStateUI.Error(error.message ?: "Error desconocido") }
                )
            } catch (e: Exception) {
                _state.value = LoginStateUI.Error(e.message ?: "Error de validación")
            }
        }
    }


}