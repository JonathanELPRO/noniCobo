package com.calyrsoft.ucbp1.features.dollar.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calyrsoft.ucbp1.features.dollar.domain.model.DollarModel
import com.calyrsoft.ucbp1.features.dollar.domain.usecase.DeleteByTimeStampUseCase
import com.calyrsoft.ucbp1.features.dollar.domain.usecase.GetDollarFromFireBaseInMyLocalDBUseCase
import com.calyrsoft.ucbp1.features.dollar.domain.usecase.GetHistoryOfDollarsFromMyLocalDBUseCase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class DollarViewModel(
    val getDollarFromFireBaseInMyLocalDBUseCase: GetDollarFromFireBaseInMyLocalDBUseCase,
    val getHistoryOfDollarsFromMyLocalDBUseCase: GetHistoryOfDollarsFromMyLocalDBUseCase,
    val deleteByTimeStampUseCase: DeleteByTimeStampUseCase
): ViewModel() {


    sealed class DollarUIState {
        object Init: DollarUIState()
        object Loading : DollarUIState()
        class Error(val message: String) : DollarUIState()
        class Success(val data: DollarModel) : DollarUIState()
        class History(val data: List<DollarModel>) : DollarUIState()

    }

    private val liveDollarFlow = getDollarFromFireBaseInMyLocalDBUseCase.invoke()
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            replay = 1
        )

    //el flow o datos en vivo que estamos recolectando solo va a vivir mientras
    //mi viewMOdel viva, el started define cuando debe empezar a compartir datos del firebase
    //WhileSubscribed → el Flow solo se conecta a Firebase si hay al menos un collector activo.
    //si en 5 segundos ya no encuentra un collector activo deja de transmitir los datos del firebase
    //Indica cuántos últimos valores se deben guardar en caché para nuevos suscriptores.
    //
    //replay = 1 → siempre guarda el último valor emitido.
    //
    //Cuando un nuevo collector llega (ej: abres la pantalla otra vez),
    // recibe inmediatamente ese último valor sin esperar a que Firebase
    // emita otro cambio.
    //shareIn hace que tengamos un solo flow, si creasemos mas de uno estariamos crando varios listeners(conjunto de callbacks) y asi mi aplicacion estaria escuchando doble
    //al crear un solo flow solo tengo a mi aplicacacion escuchando una sola vez, y puedo escuchar las veces que sea necesario con collect
    //como flow solo preparaba los listeners y no llevana a cabo como
    // tal ninguna accion que pueda demorar entonces no era necesario retornarlo como suspend en el invoke ni en el repo, el collect si puede demorar y por detras ya es suspend no

    init {
        getDollarFromFireBase()
    }

    private val _uiState = MutableStateFlow<DollarUIState>(DollarUIState.Init)
    val uiState: StateFlow<DollarUIState> = _uiState.asStateFlow()


    fun getDollarFromFireBase() {
        viewModelScope.launch(Dispatchers.IO) {
            getToken()
            liveDollarFlow.collect { data ->
                _uiState.value = DollarUIState.Success(data)
            }
        }
    }

    fun getHistoryOfDollarsFromLocalDB() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = DollarUIState.Loading
            val result = getHistoryOfDollarsFromMyLocalDBUseCase.invoke()
            result.fold(
                onSuccess = { history ->
                    _uiState.value = DollarUIState.History(history)
                },
                onFailure = { ex ->
                    _uiState.value = DollarUIState.Error(ex.message ?: "Error desconocido")
                }
            )
        }
    }

    fun deleteByTimestamp(timestamp: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = deleteByTimeStampUseCase.invoke(timestamp)
            result.fold(
                onSuccess = {
                    getHistoryOfDollarsFromLocalDB()
                },
                onFailure = { ex ->
                    _uiState.value = DollarUIState.Error(ex.message ?: "No se pudo borrar")
                }
            )
        }
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
                Log.d("FIREBASE", "FCM Token: $token")


                // Reanudar la ejecución con el token
                continuation.resume(token ?: "")
            }
    }

}
