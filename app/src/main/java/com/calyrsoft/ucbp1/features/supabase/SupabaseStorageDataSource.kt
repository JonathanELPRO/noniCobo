package com.calyrsoft.ucbp1.features.supabase

import com.calyrsoft.ucbp1.BuildConfig
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * DataSource que se encarga de subir archivos a Supabase Storage
 * y devolver la URL pública resultante.
 */
class SupabaseStorageDataSource(
    private val storageService: SupabaseStorageService
) {

    /**
     * Sube una imagen al bucket indicado y devuelve la URL pública.
     * La imagen se sube directamente desde memoria sin guardarla localmente.
     *
     * @param bucket Nombre del bucket en Supabase.
     * @param imageBytes Bytes de la imagen a subir.
     * @param filename Nombre del archivo (ej: "lodging_image.jpg").
     * @param mimeType Tipo MIME de la imagen (por defecto image/jpeg).
     */
    suspend fun uploadImageAndGetUrl(
        bucket: String,
        imageBytes: ByteArray,
        filename: String,
        mimeType: String = "image/jpeg"
    ): Result<String> {
        android.util.Log.d("SupabaseStorage", "Iniciando subida de imagen: $filename, tamaño: ${imageBytes.size} bytes")
        
        // Supabase Storage espera el campo "file" en el multipart
        // El Content-Type debe ser el MIME type de la imagen
        val mediaType = mimeType.toMediaTypeOrNull()
        val requestBody = imageBytes.toRequestBody(mediaType)
        
        // Crear el part con el nombre correcto "file"
        val part = MultipartBody.Part.createFormData(
            name = "file",
            filename = filename,
            body = requestBody
        )
        
        android.util.Log.d("SupabaseStorage", "Multipart creado: filename=$filename, contentType=$mimeType")

        // Construimos el path: solo el nombre del archivo en la raíz del bucket
        val path = filename
        android.util.Log.d("SupabaseStorage", "Subiendo a bucket: $bucket, path: $path")
        android.util.Log.d("SupabaseStorage", "URL completa sería: ${com.calyrsoft.ucbp1.BuildConfig.SUPABASE_URL}storage/v1/object/$bucket/$path")

        val response = try {
            storageService.uploadImage(
                bucket = bucket,
                path = path,
                file = part,
                upsert = "true"
            )
        } catch (e: Exception) {
            android.util.Log.e("SupabaseStorage", "Excepción al subir: ${e.message}", e)
            return Result.failure(e)
        }

        android.util.Log.d("SupabaseStorage", "Respuesta: código=${response.code()}, exitoso=${response.isSuccessful}")

        return if (response.isSuccessful) {
            val body = response.body()
            android.util.Log.d("SupabaseStorage", "Body recibido: $body")
            
            if (body != null) {
                // Como el bucket es público, construimos la URL pública.
                // El campo Key viene con el formato "TelosJoinBucket/archivo.jpg" o "TelosJoinBucket/lodgings/archivo.jpg"
                // Aseguramos que SUPABASE_URL termine con "/" para construir la URL correctamente
                val baseUrl = if (BuildConfig.SUPABASE_URL.endsWith("/")) {
                    BuildConfig.SUPABASE_URL
                } else {
                    "${BuildConfig.SUPABASE_URL}/"
                }
                val publicUrl = "${baseUrl}storage/v1/object/public/${body.Key}"
                android.util.Log.d("SupabaseStorage", "URL pública generada desde body: $publicUrl")
                Result.success(publicUrl)
            } else {
                // Si no hay body pero fue exitoso, construimos la URL manualmente
                val baseUrl = if (BuildConfig.SUPABASE_URL.endsWith("/")) {
                    BuildConfig.SUPABASE_URL
                } else {
                    "${BuildConfig.SUPABASE_URL}/"
                }
                val publicUrl = "${baseUrl}storage/v1/object/public/$bucket/$path"
                android.util.Log.d("SupabaseStorage", "URL pública generada (sin body): $publicUrl")
                Result.success(publicUrl)
            }
        } else {
            val errorBody = try {
                val errorBodyString = response.errorBody()?.string()
                android.util.Log.e("SupabaseStorage", "Error body completo: $errorBodyString")
                errorBodyString ?: "Sin detalles"
            } catch (e: Exception) {
                android.util.Log.e("SupabaseStorage", "Error al leer errorBody: ${e.message}", e)
                "Error al leer errorBody: ${e.message}"
            }
            android.util.Log.e("SupabaseStorage", "Error ${response.code()}: ${response.message()}. Body: $errorBody")
            android.util.Log.e("SupabaseStorage", "Headers de respuesta: ${response.headers()}")
            Result.failure(
                Exception(
                    "Error al subir imagen: ${response.code()} ${response.message()}. Detalles: $errorBody"
                )
            )
        }
    }
}


