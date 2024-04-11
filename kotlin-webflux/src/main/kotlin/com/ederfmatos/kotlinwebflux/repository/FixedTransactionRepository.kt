package com.ederfmatos.kotlinwebflux.repository

import com.ederfmatos.kotlinwebflux.entity.Transaction
import com.ederfmatos.kotlinwebflux.enums.Currency
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.math.BigDecimal.TEN
import java.time.Duration
import java.time.ZonedDateTime

@Repository
@ConditionalOnProperty(value = ["repository.type"], havingValue = "fixed", matchIfMissing = true)
class FixedTransactionRepository : TransactionRepository {

    override fun create(transaction: Transaction): Mono<Void> {
        return Mono.delay(Duration.ofMillis(20))
            .subscribeOn(Schedulers.boundedElastic())
            .flatMap { Mono.empty() }
    }

    override fun findById(id: String): Mono<Transaction> {
        return Mono.delay(Duration.ofMillis(20))
            .subscribeOn(Schedulers.boundedElastic())
            .map { Transaction(id, "description", Currency.BRL, TEN, TEN, ZonedDateTime.now()) }
    }
}