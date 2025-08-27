package com.calyrsoft.ucbp1.domain.repository

import com.calyrsoft.ucbp1.domain.model.ExchangeRateModel

interface IExchangeRateRepository {
    fun getExchangeRate(currency: String): Result<ExchangeRateModel>
}