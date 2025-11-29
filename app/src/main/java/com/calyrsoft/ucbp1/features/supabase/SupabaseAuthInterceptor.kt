package com.example.ucbp1.interceptors.supabase

import android.util.Log
import com.calyrsoft.ucbp1.dataStore.AuthDataStore
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class SupabaseAuthInterceptor(
    private val apiKey: String,
    private val authDataStore: AuthDataStore
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
            .addHeader("apikey", apiKey)
        
        // Agregar el token de autorización si está disponible
        val tokenResult = runBlocking {
            authDataStore.getToken()
        }
        
        tokenResult.onSuccess { token ->
            if (token.isNotBlank()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
                Log.d("SupabaseAuthInterceptor", "Token agregado a la petición")
            } else {
                Log.w("SupabaseAuthInterceptor", "Token vacío, no se agrega Authorization header")
            }
        }.onFailure { exception ->
            Log.w("SupabaseAuthInterceptor", "No se pudo obtener el token: ${exception.message}")
        }
        
        val request = requestBuilder.build()
        val response = chain.proceed(request)
        
        // Si recibimos un 403 con token expirado, loguear para debugging
        if (response.code == 403) {
            val errorBody = response.peekBody(1024).string()
            if (errorBody.contains("exp") || errorBody.contains("expired") || errorBody.contains("timestamp check failed")) {
                Log.e("SupabaseAuthInterceptor", "Token expirado. El usuario necesita volver a iniciar sesión.")
            }
        }
        
        return response
    }
}
