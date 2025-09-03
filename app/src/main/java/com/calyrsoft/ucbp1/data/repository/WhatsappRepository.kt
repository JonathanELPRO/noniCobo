package com.calyrsoft.ucbp1.data.repository

import com.calyrsoft.ucbp1.domain.repository.IWhatsappRepository

class WhatsappRepository : IWhatsappRepository {

    private val numbers = mutableListOf(
        "+59171234567",
        "+59178901234",
        "+59170112233"
    )

    override fun getFirstNumber(): String {
        return numbers.firstOrNull() ?: throw Exception("No hay números disponibles")
    }

}