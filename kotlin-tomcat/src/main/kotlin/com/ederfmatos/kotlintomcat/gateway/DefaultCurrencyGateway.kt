package com.ederfmatos.kotlintomcat.gateway

import com.ederfmatos.kotlintomcat.enums.Currency
import com.ederfmatos.kotlintomcat.log.Logger
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import java.math.BigDecimal
import java.util.UUID

@Component
class DefaultCurrencyGateway : CurrencyGateway {
    private companion object {
        val logger = Logger.create(DefaultCurrencyGateway::class)
    }

    private val restClient: RestClient = RestClient.create("http://localhost:8090")

    override fun getCurrencies(): Map<Currency, BigDecimal> {
        logger.info { withMessage("Sending request to get currencies") }
        return restClient.get()
            .uri("/currencies")
            .header("X-Request-ID", UUID.randomUUID().toString())
            .retrieve()
            .body<Map<Currency, BigDecimal>>()!!
            .also {
                logger.info {
                    withMessage("Get currencies successfully")
                    withParam("currencies", it)
                }
            }
    }
}