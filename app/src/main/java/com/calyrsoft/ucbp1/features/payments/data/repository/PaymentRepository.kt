package com.calyrsoft.ucbp1.features.payments.data.repository
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.calyrsoft.ucbp1.features.payments.domain.model.PaymentModel
import com.calyrsoft.ucbp1.features.payments.domain.repository.IPaymentRepository

class PaymentRepository(
    private val context: Context
) : IPaymentRepository {

    override suspend fun sendPaymentViaWhatsApp(payment: PaymentModel): Result<Unit> {
        return try {
            val phone = payment.ownerNumber ?: return Result.failure(Exception("Número no disponible"))

            // 🔹 Construir mensaje
            val message = buildString {
                appendLine("💳 *Solicitud de pago*")
                appendLine("────────────────────")
                appendLine("👤 Usuario: ${payment.userName}")
                appendLine("🏨 Alojamiento: ${payment.lodgingName}")
                appendLine("🛏 Habitación: ${payment.selectedRoom?.name ?: "No especificada"}")
                appendLine("📆 Tipo de estadía: ${payment.selectedStay?.name ?: "No especificada"}")
                if (payment.selectedStay?.name?.contains("HORA") == true && !payment.hours.isNullOrEmpty()) {
                    appendLine("⏰ Duración: ${payment.hours} hr")
                }
                appendLine("🕐 Hora de llegada: ${if (payment.arrivalTime.isNotEmpty()) payment.arrivalTime else "No especificada"}")
                appendLine("💰 Total: Bs. ${"%.2f".format(payment.selectedPrice)}")
            }

            val uri = Uri.parse("https://wa.me/${payment.ownerNumber}?text=${Uri.encode(message)}")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
