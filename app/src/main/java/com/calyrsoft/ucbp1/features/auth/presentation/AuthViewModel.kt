package com.calyrsoft.ucbp1.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calyrsoft.ucbp1.features.auth.domain.model.Role
import com.calyrsoft.ucbp1.features.auth.domain.model.User
import com.calyrsoft.ucbp1.features.auth.domain.usecase.GetCurrentUserUseCase
import com.calyrsoft.ucbp1.features.auth.domain.usecase.LoginUseCase
import com.calyrsoft.ucbp1.features.auth.domain.usecase.RegisterUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    sealed class AuthStateUI {
        object Init : AuthStateUI()
        object Loading : AuthStateUI()
        data class Success(val user: User) : AuthStateUI()
        data class Error(val message: String) : AuthStateUI()
    }

    private val _state = MutableStateFlow<AuthStateUI>(AuthStateUI.Init)
    val state: StateFlow<AuthStateUI> = _state

    fun login(userOrEmail: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthStateUI.Loading
            val result = loginUseCase(userOrEmail, password)
            result
                .onSuccess { user -> _state.value = AuthStateUI.Success(user) }
                .onFailure { e -> _state.value = AuthStateUI.Error(e.message ?: "Error al iniciar sesión") }
        }
    }

    fun register(
        username: String,
        email: String,
        phone: String?,
        password: String,
        role: Role
    ) {
        viewModelScope.launch {
            _state.value = AuthStateUI.Loading
            val registerResult = registerUserUseCase(
                User(username = username, email = email, phone = phone, role = role),
                password
            )

            registerResult
                .onSuccess { id ->
                    val userResult = getCurrentUserUseCase(id)
                    userResult
                        .onSuccess { user -> _state.value = AuthStateUI.Success(user) }
                        .onFailure { e -> _state.value = AuthStateUI.Error(e.message ?: "Error al obtener usuario") }
                }
                .onFailure { e ->
                    _state.value = AuthStateUI.Error(e.message ?: "Error al registrar usuario")
                }
        }
    }

    fun logout() {
        _state.value = AuthStateUI.Init
    }
}
