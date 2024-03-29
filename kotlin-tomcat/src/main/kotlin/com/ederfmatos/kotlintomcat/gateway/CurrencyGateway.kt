package com.ederfmatos.kotlintomcat.gateway

import com.ederfmatos.kotlintomcat.enums.Currency
import java.math.BigDecimal

interface CurrencyGateway {
    fun getCurrencies(): Map<Currency, BigDecimal>
}