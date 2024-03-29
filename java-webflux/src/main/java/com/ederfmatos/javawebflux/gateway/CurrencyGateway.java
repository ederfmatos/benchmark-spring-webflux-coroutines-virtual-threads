package com.ederfmatos.javawebflux.gateway;

import com.ederfmatos.javawebflux.enums.Currency;
import java.math.BigDecimal;
import java.util.Map;
import reactor.core.publisher.Mono;

public sealed interface CurrencyGateway permits DefaultCurrencyGateway {
    Mono<Map<Currency, BigDecimal>> getCurrencies();
}
