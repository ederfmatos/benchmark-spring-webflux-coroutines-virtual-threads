package com.ederfmatos.kotlinwebflux.gateway

import com.ederfmatos.kotlinwebflux.enums.Currency
import reactor.core.publisher.Mono
import java.math.BigDecimal

interface CurrencyGateway {
    fun getCurrencies(): Mono<Map<Currency, BigDecimal>>
}