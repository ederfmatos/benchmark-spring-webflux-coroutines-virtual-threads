package com.ederfmatos.kotlincoroutines.gateway

import com.ederfmatos.kotlincoroutines.enums.Currency
import java.math.BigDecimal

interface CurrencyGateway {
    suspend fun getCurrencies(): Map<Currency, BigDecimal>
}