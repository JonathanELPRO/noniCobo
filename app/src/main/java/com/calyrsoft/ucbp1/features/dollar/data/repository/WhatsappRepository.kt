package com.calyrsoft.ucbp1.features.dollar.data.repository

import com.calyrsoft.ucbp1.features.dollar.domain.repository.IWhatsappRepository

class WhatsappRepository : IWhatsappRepository {

    private val numbers = mutableListOf(
        "+59171234567",
        "+59178901234",
        "+59170112233"
    )

    override fun getFirstNumber(): Result<String> {
        val number = numbers.firstOrNull()
        return if (number != null) {
            Result.success(number)
        } else {
            Result.failure(Exception("No hay números disponibles"))
        }
    }

}