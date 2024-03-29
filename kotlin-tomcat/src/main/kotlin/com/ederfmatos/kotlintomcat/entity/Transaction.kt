package com.ederfmatos.kotlintomcat.entity

import com.ederfmatos.kotlintomcat.enums.Currency
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.UUID

data class Transaction(
    val id: String = UUID.randomUUID().toString(),
    val description: String,
    val currency: Currency,
    val amount: BigDecimal,
    val totalAmount: BigDecimal,
    val createdAt: ZonedDateTime
) {

    companion object {
        fun create(description: String, totalAmount: BigDecimal, amount: BigDecimal, currency: Currency): Transaction {
            return Transaction(
                description = description,
                currency = currency,
                amount = amount,
                totalAmount = totalAmount,
                createdAt = ZonedDateTime.now()
            )
        }
    }
}