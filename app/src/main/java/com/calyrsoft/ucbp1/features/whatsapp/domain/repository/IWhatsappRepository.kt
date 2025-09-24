package com.calyrsoft.ucbp1.features.whatsapp.domain.repository

interface IWhatsappRepository {
    fun getFirstNumber(): Result<String>
}