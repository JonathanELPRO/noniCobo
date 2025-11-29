package com.calyrsoft.ucbp1.features.supabase

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Servicio Retrofit para interactuar con Supabase Storage.
 *
 * Importante: este servicio usa la misma instancia Retrofit configurada
 * con la URL base de Supabase y el interceptor de autenticación.
 */
data class SupabaseUploadResponse(
    // Supabase devuelve algo similar a "TelosJoinBucket/ruta/archivo.jpg"
    val Key: String
)

interface SupabaseStorageService {

    /**
     * Sube un archivo al bucket indicado.
     *
     * Endpoint de Supabase Storage:
     * POST /storage/v1/object/{bucket}/{path}
     * 
     * Nota: El path debe estar codificado en URL si contiene caracteres especiales.
     * 
     * @param bucket Nombre del bucket (ej: "TelosJoinBucket")
     * @param path Ruta del archivo dentro del bucket (ej: "lodgings/archivo.jpg")
     * @param file Parte multipart con el archivo
     * @param upsert Header para sobrescribir si el archivo ya existe
     */
    @Multipart
    @POST("/storage/v1/object/{bucket}/{path}")
    suspend fun uploadImage(
        @Path("bucket") bucket: String,
        @Path("path") path: String,
        @Part file: MultipartBody.Part,
        @Header("x-upsert") upsert: String = "true"
    ): Response<SupabaseUploadResponse>
}


