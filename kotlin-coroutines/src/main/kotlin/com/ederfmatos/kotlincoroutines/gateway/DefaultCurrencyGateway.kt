package com.ederfmatos.kotlincoroutines.gateway

import com.ederfmatos.kotlincoroutines.enums.Currency
import com.ederfmatos.kotlincoroutines.log.Logger
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.math.BigDecimal
import java.util.UUID

@Component
class DefaultCurrencyGateway : CurrencyGateway {
    private companion object {
        val logger = Logger.create(DefaultCurrencyGateway::class)
    }

    private val webClient: WebClient = WebClient.create("http://localhost:8090")

    override suspend fun getCurrencies(): Map<Currency, BigDecimal> {
        logger.info { withMessage("Sending request to get currencies") }
        return webClient.get()
            .uri("/currencies")
            .header("X-Request-ID", UUID.randomUUID().toString())
            .retrieve()
            .awaitBody<Map<Currency, BigDecimal>>()
            .also {
                logger.info {
                    withMessage("Get currencies successfully")
                    withParam("currencies", it)
                }
            }
    }
}