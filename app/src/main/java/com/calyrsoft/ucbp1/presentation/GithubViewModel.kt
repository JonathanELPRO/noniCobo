package com.calyrsoft.ucbp1.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calyrsoft.ucbp1.domain.model.UserModel
import com.calyrsoft.ucbp1.domain.usecase.FindByNickNameUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class GithubViewModel(
    val useCase: FindByNickNameUseCase
): ViewModel() {
    sealed class GithubStateUI {
        object Init: GithubStateUI()
        object Loading: GithubStateUI()
        class Error(val message: String) : GithubStateUI()
        class Success(val github: UserModel) : GithubStateUI()

        //los que son object es porque sus datos internos jamas cambiaran, los que son class son class
        //porque sus datos internos si pueden llegar a cambiar
    }

    //Una clase sellada (sealed class) en Kotlin es una clase que solo puede ser heredada dentro del mismo archivo donde se declara.

    private val _state = MutableStateFlow<GithubStateUI>(GithubStateUI.Init)
    //_state representa el estado en el que se encuentra mi pantalla, en este caso los estados se pasan mandando
    // la clase sellada entre <>
    //ademas este estado necesita tener un valor inicial y sera el valor del estado Init eso se pasa con: GithubStateUI.Init
    val state: StateFlow<GithubStateUI> = _state.asStateFlow()
    //ESTA VARIABle sigue representado en estado de la vista la unica diferencia es que este solo te permite observar ese estado
    //a diferencia del _state que te permitia cambiarlo, en si con StateFlow estamos creando una variable observable
    //que cada que cambia notifica a aquellos que la estan observando

    //lo de arriba es importante para tener a una cosa que solo se encarga de setear, y a otra que no setea
    //pero monitorea esos seteos

    //como soy el orquestador voy a trabajar con el caso de uso abajo
    //esto de abajo es el puente
    fun fetchAlias(nickname: String) {
        //este en si es el puente, pues aqui se usara un caso de uso, y este metodo sera llamado desde el screen
        //y nos debemos a poner a pensar en que hara en como funcionara este fetch alias?, pues podemos decir que si es
        //un puente entre el boton(vista) y el caso de uso, pues se presionara un boton y se llamara a este fecth alias
        //y luego a un caso de uso pero incluso antes de llamar a ese caso de uso se hacen algunas otras cosas como:
        //setear el estado de nuestra vista pues antes de llamar al caso de uso debemos decir que nuestra vista se esta cargando
        viewModelScope.launch(Dispatchers.IO) {
            //launch crea una corutina=hilo ligero, ideal por si quiero hacer varias actividades simultaneamente,
            //sin usar varios hilos diferentes
            //dispatchers hace que este hilo ligero sea mas eficiente

            _state.value = GithubStateUI.Loading
            //arriba podras notar que seteamos el estado de la vista incluso antes de llamar al caso de uso

            val result = useCase.invoke(nickname)
            //arriba estamos usando al caso de uso

            result.fold(
                onSuccess = { user ->
                    _state.value = GithubStateUI.Success(user)
                },
                //si el result que nos llego no tuvo errores llamamos a una funcion lambda o sin nombre
                // parametroRecibido -> Hace algo con el parametro
                //ese parametro recibido nos llegar del result pues resulto exitoso en este caso deberia ser un UserModel
                //y agarraremos ese userModel y setearemos el estado de nuestra pantalla con _state.value
                //en este caso le daremos el valor de uno de los estados que definimos antes: GithubStateUI.Success(user)
                //esto es correcto porque si nos vamos a la definicion de ese estado pues si esta esperando un UserModel
                onFailure = { error ->
                    _state.value = GithubStateUI.Error(message = error.message ?: "Error desconocido")
                }
                //error viene de la informacion del fold del result
            )

//            Result funciona como una “cajita” que puede contener el valor de éxito o el error (excepción), muy parecido a lo que pasa con un try/catch.
//
//            Con fold lo que haces es desempaquetar esa cajita y decidir
        }
    }

}