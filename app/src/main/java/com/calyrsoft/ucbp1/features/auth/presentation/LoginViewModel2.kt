package com.calyrsoft.ucbp1.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calyrsoft.ucbp1.features.auth.domain.model.User
import com.calyrsoft.ucbp1.features.auth.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel2(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    sealed class LoginUIState {
        object Init : LoginUIState()
        object Loading : LoginUIState()
        data class Success(val user: User) : LoginUIState()
        data class Error(val message: String) : LoginUIState()
    }

    private val _state = MutableStateFlow<LoginUIState>(LoginUIState.Init)
    val state: StateFlow<LoginUIState> = _state

    fun login(userOrEmail: String, password: String) {
        viewModelScope.launch {
            _state.value = LoginUIState.Loading
            val result = loginUseCase(userOrEmail, password)
            result
                .onSuccess { user -> _state.value = LoginUIState.Success(user) }
                .onFailure { e -> _state.value = LoginUIState.Error(e.message ?: "Error al iniciar sesión") }
        }
    }

    fun resetState() {
        _state.value = LoginUIState.Init
    }
}
