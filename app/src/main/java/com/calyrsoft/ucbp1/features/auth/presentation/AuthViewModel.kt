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

data class AuthUiState(
    val currentUser: User? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> get() = _uiState

    fun login(userOrEmail: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val user = loginUseCase(userOrEmail, password)
                _uiState.value = _uiState.value.copy(currentUser = user, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message, isLoading = false)
            }
        }
    }

    fun register(username: String, email: String, phone: String?, password: String, role: Role = Role.CLIENT) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val id = registerUserUseCase(
                    User(username = username, email = email, phone = phone, role = role),
                    password
                )
                val newUser = getCurrentUserUseCase(id)
                _uiState.value = _uiState.value.copy(currentUser = newUser, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message, isLoading = false)
            }
        }
    }

    fun logout() {
        _uiState.value = AuthUiState()
    }
}
