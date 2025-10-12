package com.calyrsoft.ucbp1.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calyrsoft.ucbp1.features.auth.domain.model.Role
import com.calyrsoft.ucbp1.features.auth.domain.model.User
import com.calyrsoft.ucbp1.features.auth.domain.usecase.GetCurrentUserUseCase
import com.calyrsoft.ucbp1.features.auth.domain.usecase.RegisterUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUserUseCase: RegisterUserUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    sealed class RegisterUIState {
        object Init : RegisterUIState()
        object Loading : RegisterUIState()
        data class Success(val user: User) : RegisterUIState()
        data class Error(val message: String) : RegisterUIState()
    }

    private val _state = MutableStateFlow<RegisterUIState>(RegisterUIState.Init)
    val state: StateFlow<RegisterUIState> = _state

    fun register(username: String, email: String, phone: String?, password: String, role: Role) {
        viewModelScope.launch {
            _state.value = RegisterUIState.Loading

            val registerResult = registerUserUseCase(
                User(username = username, email = email, phone = phone, role = role),
                password
            )

            registerResult
                .onSuccess { id ->
                    val userResult = getCurrentUserUseCase(id)
                    userResult
                        .onSuccess { user -> _state.value = RegisterUIState.Success(user) }
                        .onFailure { e -> _state.value = RegisterUIState.Error(e.message ?: "Error al obtener usuario") }
                }
                .onFailure { e ->
                    _state.value = RegisterUIState.Error(e.message ?: "Error al registrar usuario")
                }
        }
    }

    fun resetState() {
        _state.value = RegisterUIState.Init
    }
}
