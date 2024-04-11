package com.ederfmatos.kotlintomcat.repository

import com.ederfmatos.kotlintomcat.entity.Transaction
import com.ederfmatos.kotlintomcat.enums.Currency
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Repository
import java.math.BigDecimal.TEN
import java.time.ZonedDateTime

@Repository
@ConditionalOnProperty(value = ["repository.type"], havingValue = "fixed", matchIfMissing = true)
class FixedTransactionRepository : TransactionRepository {
    override fun create(transaction: Transaction) {
        Thread.sleep(20)
    }

    override fun findById(id: String): Transaction? {
        Thread.sleep(20)
        return Transaction(id, "description", Currency.BRL, TEN, TEN, ZonedDateTime.now())
    }
}