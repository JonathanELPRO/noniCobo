package com.calyrsoft.ucbp1.data.repository

import com.calyrsoft.ucbp1.domain.model.ExchangeRateModel
import com.calyrsoft.ucbp1.domain.repository.IExchangeRateRepository

class ExchangeRateRepository : IExchangeRateRepository {

    private val rates = listOf(
        ExchangeRateModel(baseCurrency = "BOB", currency = "USD", buyRate = 6.96, sellRate = 6.99),
        ExchangeRateModel(baseCurrency = "BOB", currency = "EUR", buyRate = 7.50, sellRate = 7.55),
        ExchangeRateModel(baseCurrency = "BOB", currency = "JPY", buyRate = 0.051, sellRate = 0.052),
        ExchangeRateModel(baseCurrency = "BOB", currency = "GBP", buyRate = 8.80, sellRate = 8.85)
    )

    override fun getExchangeRate(currency: String): Result<ExchangeRateModel> {
        val exchangeRate = rates.find { it.currency.equals(currency, ignoreCase = true) }
        return if (exchangeRate != null) Result.success(exchangeRate)
        else Result.failure(Exception("Moneda no encontrada"))
    }
}