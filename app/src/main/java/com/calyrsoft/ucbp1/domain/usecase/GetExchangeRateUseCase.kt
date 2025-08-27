package com.calyrsoft.ucbp1.domain.usecase

import com.calyrsoft.ucbp1.domain.model.ExchangeRateModel
import com.calyrsoft.ucbp1.domain.repository.IExchangeRateRepository

class GetExchangeRateUseCase(private val repository: IExchangeRateRepository) {
    fun invoke(currency: String): Result<ExchangeRateModel> {
        return repository.getExchangeRate(currency)
    }
}