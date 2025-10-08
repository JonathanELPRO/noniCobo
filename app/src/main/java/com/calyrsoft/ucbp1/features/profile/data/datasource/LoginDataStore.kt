package com.calyrsoft.ucbp1.features.profile.data.datasource

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.dataStore by preferencesDataStore(name = "user_preferences")
//Esto crea una extensión de Context llamada dataStore.

//preferencesDataStore(name = "user_preferences")
//arriba estamos creando un data store llamado user_preferences, asu vez se crea
// un lugar llamado preferences_pb donde se guardaran datos

class LoginDataStore(
    private val context: Context
) {
    companion object {
        val USER_NAME = stringPreferencesKey("user_name")
        val TOKEN = stringPreferencesKey("token")
    }

    suspend fun saveUserName(userName: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME] = userName
        }
        //.edit Abre una transacción segura de escritura sobre el archivo .preferences_pb.
        //em preferences accedemos a todos los datos que guardamos en .preferences_pb
    }

    suspend fun getUserName(): Result<String> {
        val preferences = context.dataStore.data.first()
        //.data retorna un Flow<Preferences> osea algo con lo que podemos
        //monitorear en vivo preferences, recordemos que ahi accedemos a los datos de preferences_pb
        //con first solo accedemos a esos datos en vivo una sola vez osea solo obtenemos un valor emitido

        return preferences[USER_NAME]?.let {
            Result.success(it)
        } ?: Result.failure(Exception("User name not found"))
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN] = token
        }
    }

    suspend fun getToken(): Result<String> {
        val preferences = context.dataStore.data.first()
        return preferences[TOKEN]?.let {
            Result.success(it)
        } ?: Result.failure(Exception("Token not found"))
    }
}
