package com.calyrsoft.ucbp1.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calyrsoft.ucbp1.domain.model.LoginUserModel
import com.calyrsoft.ucbp1.domain.usecase.FindByNameAndPasswordUseCase
import com.calyrsoft.ucbp1.domain.usecase.GetFirstWhatsappNumberUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    val useCase: FindByNameAndPasswordUseCase,
    val getFirstWhatsappNumberUseCase: GetFirstWhatsappNumberUseCase
): ViewModel()  {
    sealed class LoginStateUI {
        object Init: LoginStateUI()
        object Loading: LoginStateUI()
        class Error(val message: String) : LoginStateUI()
        class Success(val loginUser: LoginUserModel) : LoginStateUI()

    }

    private val _state = MutableStateFlow<LoginViewModel.LoginStateUI>(LoginViewModel.LoginStateUI.Init)

    val state: StateFlow<LoginViewModel.LoginStateUI> = _state.asStateFlow()

    fun fetchAlias(name: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {


            _state.value = LoginStateUI.Loading

            val result = useCase.invoke(name, password)

            result.fold(
                onSuccess = { user ->
                    _state.value = LoginStateUI.Success(user)
                },

                onFailure = { error ->
                    _state.value = LoginStateUI.Error(message = error.message ?: "Error desconocido")
                }
            )


        }
    }

    fun openWhatsapp(): String {
        return getFirstWhatsappNumberUseCase.invoke()
    }

}