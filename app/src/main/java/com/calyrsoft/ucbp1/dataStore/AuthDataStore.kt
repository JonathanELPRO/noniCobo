package com.calyrsoft.ucbp1.dataStore


import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

// Extensión de DataStore
val Context.dataStore by preferencesDataStore(name = "user_preferences")

class AuthDataStore(
    private val context: Context
) {
    val isLoggedInFlow = context.dataStore.data.map { prefs ->
        prefs[TOKEN] != null && prefs[USER_EMAIL] != null && prefs[ROLE] != null && prefs[ID] != null
    }

    val userRoleFlow = context.dataStore.data.map { prefs ->
        prefs[ROLE]
    }

    val userIdFlow = context.dataStore.data.map { prefs ->
        prefs[ID]
    }

    companion object {
        val USER_EMAIL = stringPreferencesKey("email")

        val ID = longPreferencesKey("id")

        val TOKEN = stringPreferencesKey("token")

        val ROLE = stringPreferencesKey("role")

        val USERNAME = stringPreferencesKey("username")



    }

    // Guardar email
    suspend fun saveUserEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_EMAIL] = email
        }
    }

    // Guardar token
    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN] = token
        }
    }


    // Guardar id
    suspend fun saveId(id: Long) {
        context.dataStore.edit { preferences ->
            preferences[ID] = id
        }
    }

    suspend fun saveUsername(username: String) {
        context.dataStore.edit { preferences ->
            preferences[USERNAME] = username
        }
    }


    // Guardar ambos a la vez
    suspend fun saveUser(email: String, token: String,role:String) {
        context.dataStore.edit { preferences ->
            preferences[USER_EMAIL] = email
            preferences[TOKEN] = token
            preferences[ROLE] = role
        }
    }

    // Obtener email
    suspend fun getUserEmail(): Result<String> {
        val preferences = context.dataStore.data.first()
        return preferences[USER_EMAIL]?.let {
            Result.success(it)
        } ?: Result.failure(Exception("User email not found"))
    }

    // Obtener token
    suspend fun getToken(): Result<String> {
        val preferences = context.dataStore.data.first()
        return preferences[TOKEN]?.let {
            Result.success(it)
        } ?: Result.failure(Exception("Token not found"))
    }

    // Obtener rol
    suspend fun getRole(): Result<String> {
        val preferences = context.dataStore.data.first()
        return preferences[ROLE]?.let {
            Result.success(it)
        } ?: Result.failure(Exception("Role not found"))
    }

    // Obtener id
    suspend fun getId(): Result<Long> {
        val preferences = context.dataStore.data.first()
        return preferences[ID]?.let {
            Result.success(it)
        } ?: Result.failure(Exception("Role not found"))
    }

    suspend fun getUsername(): Result<String> {
        val preferences = context.dataStore.data.first()
        return preferences[USERNAME]?.let {
            Result.success(it)
        } ?: Result.failure(Exception("Username not found"))
    }

    // Obtener ambos
    suspend fun getUser(): Result<Pair<String, String>> {
        val preferences = context.dataStore.data.first()
        val email = preferences[USER_EMAIL]
        val token = preferences[TOKEN]
        return if (email != null && token != null) {
            Result.success(Pair(email, token))
        } else {
            Result.failure(Exception("User data not found"))
        }
    }

    // Limpiar datos (logout)
    suspend fun clearUser() {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_EMAIL)
            preferences.remove(TOKEN)
            preferences.remove(ROLE)
            preferences.remove(ID)
            preferences.clear()
        }
    }
}
