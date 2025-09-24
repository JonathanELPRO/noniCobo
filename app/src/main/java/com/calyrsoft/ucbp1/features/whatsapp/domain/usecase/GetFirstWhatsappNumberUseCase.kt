package com.calyrsoft.ucbp1.features.whatsapp.domain.usecase

import com.calyrsoft.ucbp1.features.whatsapp.domain.repository.IWhatsappRepository

class GetFirstWhatsappNumberUseCase(private val repository: IWhatsappRepository) {
    fun invoke(): Result<String> {
        return repository.getFirstNumber()
    }
}