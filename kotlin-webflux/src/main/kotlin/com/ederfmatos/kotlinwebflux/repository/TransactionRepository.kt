package com.ederfmatos.kotlinwebflux.repository

import com.ederfmatos.kotlinwebflux.entity.Transaction
import reactor.core.publisher.Mono

interface TransactionRepository {
    fun create(transaction: Transaction): Mono<Void>
    fun findById(id: String): Mono<Transaction>
}
