package com.calyrsoft.ucbp1.domain.usecase

import com.calyrsoft.ucbp1.domain.repository.IWhatsappRepository

class GetFirstWhatsappNumberUseCase(private val repository: IWhatsappRepository) {
    fun invoke(): Result<String> {
        return repository.getFirstNumber()
    }
}