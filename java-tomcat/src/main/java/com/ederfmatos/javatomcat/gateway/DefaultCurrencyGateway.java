package com.ederfmatos.javatomcat.gateway;

import com.ederfmatos.javatomcat.enums.Currency;
import com.ederfmatos.javatomcat.log.Logger;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public final class DefaultCurrencyGateway implements CurrencyGateway {
    private static final Logger logger = Logger.create(DefaultCurrencyGateway.class);
    private final RestClient restClient = RestClient.create("http://localhost:8090");

    @Override
    public Map<Currency, BigDecimal> getCurrencies() {
        logger.info(log -> log.withMessage("Sending request to get currencies"));
        var currencies = restClient.get()
            .uri("/currencies")
            .header("X-Request-ID", UUID.randomUUID().toString())
            .retrieve()
            .body(new ParameterizedTypeReference<Map<Currency, BigDecimal>>() {});
        logger.info(log -> log.withMessage("Get currencies successfully").withParam("currencies", currencies));
        return currencies;
    }
}
