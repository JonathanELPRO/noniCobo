package com.calyrsoft.ucbp1.features.lodging.domain.usecase

import android.content.Context
import android.net.Uri
import com.calyrsoft.ucbp1.features.supabase.SupabaseStorageDataSource
import java.util.UUID

class UploadImageToSupabaseUseCase(
    private val storageDataSource: SupabaseStorageDataSource,
    private val context: Context
) {
    suspend operator fun invoke(localUri: String?, imageType: String): Result<String?> {
        if (localUri == null) return Result.success(null)
        
        // Si ya es una URL remota, no subimos nada
        if (localUri.startsWith("http")) return Result.success(localUri)

        return try {
            val uri = Uri.parse(localUri)
            val inputStream = context.contentResolver.openInputStream(uri)
                ?: return Result.failure(Exception("No se pudo abrir el archivo de imagen"))

            // Leemos la imagen directamente en memoria sin guardarla localmente
            val imageBytes = inputStream.use { it.readBytes() }

            // Generamos un nombre único para el archivo
            val filename = "${imageType}_${UUID.randomUUID()}.jpg"

            storageDataSource.uploadImageAndGetUrl(
                bucket = "TelosJoinBucket",
                imageBytes = imageBytes,
                filename = filename
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

