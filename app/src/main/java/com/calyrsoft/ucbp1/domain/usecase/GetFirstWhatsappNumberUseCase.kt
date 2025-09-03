package com.calyrsoft.ucbp1.domain.usecase

import com.calyrsoft.ucbp1.domain.repository.IWhatsappRepository

class GetFirstWhatsappNumberUseCase(private val repository: IWhatsappRepository) {
    fun invoke(): String {
        return repository.getFirstNumber()
    }
}