package com.ederfmatos.kotlincoroutines.repository

import com.ederfmatos.kotlincoroutines.entity.Transaction
import com.ederfmatos.kotlincoroutines.enums.Currency
import kotlinx.coroutines.delay
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Repository
import java.math.BigDecimal.TEN
import java.time.ZonedDateTime

@Repository
@ConditionalOnProperty(value = ["repository.type"], havingValue = "fixed", matchIfMissing = true)
class FixedTransactionRepository : TransactionRepository {

    override suspend fun create(transaction: Transaction) {
        delay(20)
    }

    override suspend fun findById(id: String): Transaction? {
        delay(20)
        return Transaction(id, "description", Currency.BRL, TEN, TEN, ZonedDateTime.now())
    }
}