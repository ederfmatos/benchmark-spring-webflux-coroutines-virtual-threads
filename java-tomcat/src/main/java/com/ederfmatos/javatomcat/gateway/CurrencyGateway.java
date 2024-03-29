package com.ederfmatos.javatomcat.gateway;

import com.ederfmatos.javatomcat.enums.Currency;
import java.math.BigDecimal;
import java.util.Map;

public sealed interface CurrencyGateway permits DefaultCurrencyGateway {
    Map<Currency, BigDecimal> getCurrencies();
}
