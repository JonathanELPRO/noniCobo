package com.calyrsoft.ucbp1.features.remoteconfig.data.manager

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.ConfigUpdateListenerRegistration
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

import kotlinx.coroutines.tasks.await

object RemoteConfigManager {
    private val TAG = "RemoteConfigManager"
    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
    private var listenerRegistration: ConfigUpdateListenerRegistration? = null

    fun init() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0 // para pruebas en tiempo real
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.setDefaultsAsync(
            mapOf(
                "maintenance" to true,
                "maintenance_message" to "Actualizaaa"
            )
        )
    }

    suspend fun fetchAndActivate(): Boolean {
        return try {
            remoteConfig.fetchAndActivate().await()
        } catch (e: Exception) {
            Log.e(TAG, "Error al hacer fetchAndActivate", e)
            false
        }
    }
    //este lo que hace no es escuchar cambios en tiempo real, solo escucha un cambio
    //y es la primera vez
    //osea lo usaremos nada mas arrancar la aplicacion usaremos el metodo
    //fetchAndActivate que significa descarga y activa osea le da valores a
    //fun isMaintenance(): Boolean = remoteConfig.getBoolean("maintenance")
    //    fun getMessage(): String = remoteConfig.getString("maintenance_message")
    //al menos la primera vez en el arranque de la aplicacion

    fun isMaintenance(): Boolean = remoteConfig.getBoolean("maintenance")
    fun getMessage(): String = remoteConfig.getString("maintenance_message")

    fun listenRealtime(onChanged: () -> Unit) {
        listenerRegistration = remoteConfig.addOnConfigUpdateListener(
            //listenerRegistration es de tipo ConfigUpdateListenerRegistration
            //asiq eu lo estamos usando para a remote config que es el servicio como tal
            //meterle un Config update literner
            //osea alguien que escuchara los cambiso en tiempo real
            object : ConfigUpdateListener {
                //el de arriba como tal es el que esuchara los cambios en tiempo real
                override fun onUpdate(configUpdate: ConfigUpdate) {
                    Log.d(TAG, "🟡 Cambios detectados en Remote Config: ${configUpdate.updatedKeys}")
                    if (configUpdate.updatedKeys.contains("maintenance") ||
                        configUpdate.updatedKeys.contains("maintenance_message")
                    ) {
                        remoteConfig.activate().addOnCompleteListener {
                            Log.d(TAG, "✅ Config activado en tiempo real.")
                            onChanged()
                        }
                        //.activate activa los valores nuevos descargados de Firebase
                        //osea si es que se detecta cambios, los desacargamos con activate y los asiganmos a:
                        //fun isMaintenance():
                        //y el otro metodo que esta debajo de el
                    }
                }

                override fun onError(error: FirebaseRemoteConfigException) {
                    Log.e(TAG, "❌ Error en ConfigUpdateListener: ${error.code}", error)
                }
            }
        )
    }

    fun stopListening() {
        listenerRegistration?.remove()
        listenerRegistration = null
    }
}
