package com.calyrsoft.ucbp1.features.auth.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calyrsoft.ucbp1.dataStore.AuthDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch



class AuthViewModel(
    private val authDataStore: AuthDataStore
) : ViewModel() {


    // Estado reactivo: si está logueado o no
    val isLoggedIn: StateFlow<Boolean> = authDataStore.isLoggedInFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val userRole: StateFlow<String?> = authDataStore.userRoleFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val userId: StateFlow<Long?> = authDataStore.userIdFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    // Guardar usuario (login exitoso)
    fun saveUser(email: String, token: String, role: String) {
        viewModelScope.launch {
            authDataStore.saveUser(email, token, role)
        }
    }

    // Guardar id
    fun saveId(id: Long?) {
        viewModelScope.launch {
            authDataStore.saveId(id?:0)
        }
    }

    fun saveUsername(username: String?) {
        viewModelScope.launch {
            authDataStore.saveUsername(username?:"NoUsername")
        }
    }

    suspend fun getId(): Long? {
        val result = authDataStore.getId()
        return result.getOrNull()
    }

    suspend fun getUsername(): String? {
        val result = authDataStore.getUsername()
        return result.getOrNull()
    }




    // Cerrar sesión (logout)
    fun logout() {
        viewModelScope.launch {
            authDataStore.clearUser()
        }
    }
}
