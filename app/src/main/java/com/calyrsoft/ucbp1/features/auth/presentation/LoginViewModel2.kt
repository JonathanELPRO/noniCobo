package com.calyrsoft.ucbp1.features.auth.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calyrsoft.ucbp1.dataStore.AuthDataStore
import com.calyrsoft.ucbp1.features.auth.domain.model.User
import com.calyrsoft.ucbp1.features.auth.domain.usecase.LoginUseCase
import com.calyrsoft.ucbp1.features.auth.domain.usecase.LoginWithSupabaseUseCase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class LoginViewModel2(
    private val loginUseCase: LoginUseCase,
    private val loginWithSupabaseUseCase: LoginWithSupabaseUseCase,
    private val authDataStore: AuthDataStore
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
            val result= loginWithSupabaseUseCase(userOrEmail,password)
            result
                .onSuccess { user -> _state.value = LoginUIState.Success(user)
                }
                .onFailure { e -> _state.value = LoginUIState.Error(e.message ?: "Error al iniciar sesión") }
        }
    }

    fun resetState() {
        _state.value = LoginUIState.Init
    }

    suspend fun getToken(): String = suspendCoroutine { continuation ->
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FIREBASE", "getInstanceId failed", task.exception)
                continuation.resumeWithException(task.exception ?: Exception("Unknown error"))
                return@addOnCompleteListener
            }
            // Si la tarea fue exitosa, se obtiene el token
            val token = task.result
            Log.d("FIREBASE de login", "FCM Token: $token")


            // Reanudar la ejecución con el token
            continuation.resume(token ?: "")
        }
    }
}
