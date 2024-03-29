package com.ederfmatos.javawebflux.gateway;

import com.ederfmatos.javawebflux.enums.Currency;
import com.ederfmatos.javawebflux.log.Logger;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public final class DefaultCurrencyGateway implements CurrencyGateway {
    private static final Logger logger = Logger.create(DefaultCurrencyGateway.class);
    private final WebClient webClient = WebClient.create("http://localhost:8090");

    @Override
    public Mono<Map<Currency, BigDecimal>> getCurrencies() {
        logger.info(log -> log.withMessage("Sending request to get currencies"));
        return webClient.get()
                .uri("/currencies")
                .header("X-Request-ID", UUID.randomUUID().toString())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<Currency, BigDecimal>>() {})
                .doOnSuccess(currencies -> logger.info(log -> log.withMessage("Get currencies successfully").withParam("currencies", currencies)));
    }
}
