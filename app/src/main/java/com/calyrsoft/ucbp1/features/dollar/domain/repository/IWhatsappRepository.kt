package com.calyrsoft.ucbp1.features.dollar.domain.repository

interface IWhatsappRepository {
    fun getFirstNumber(): Result<String>
}