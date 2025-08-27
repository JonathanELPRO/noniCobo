package com.calyrsoft.ucbp1.domain.model

data class ExchangeRateModel(
    val baseCurrency: String = "BOB", // Moneda base
    val currency: String,              // Código de la moneda: "USD", "JPY", "GBP", etc.
    val buyRate: Double,               // Tasa para comprar esa moneda
    val sellRate: Double               // Tasa para vender esa moneda
)
