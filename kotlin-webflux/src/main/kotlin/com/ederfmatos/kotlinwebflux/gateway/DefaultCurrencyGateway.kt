package com.ederfmatos.kotlinwebflux.gateway

import com.ederfmatos.kotlinwebflux.enums.Currency
import com.ederfmatos.kotlinwebflux.log.Logger
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.util.UUID

@Component
class DefaultCurrencyGateway : CurrencyGateway {
    private companion object {
        val logger = Logger.create(DefaultCurrencyGateway::class)
    }

    private val webClient: WebClient = WebClient.create("http://localhost:8090")

    override fun getCurrencies(): Mono<Map<Currency, BigDecimal>> {
        logger.info { withMessage("Sending request to get currencies") }
        return webClient.get()
            .uri("/currencies")
            .header("X-Request-ID", UUID.randomUUID().toString())
            .retrieve()
            .bodyToMono<Map<Currency, BigDecimal>>()
            .doOnSuccess {
                logger.info {
                    withMessage("Get currencies successfully")
                    withParam("currencies", it)
                }
            }
    }
}