package com.ederfmatos.kotlinwebflux.usecase

import com.ederfmatos.kotlinwebflux.enums.Currency
import com.ederfmatos.kotlinwebflux.exception.TransactionNotFoundException
import com.ederfmatos.kotlinwebflux.repository.TransactionRepository
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.math.BigDecimal

@Component
class GetTransaction(private val transactionRepository: TransactionRepository) {

    fun execute(input: Input): Mono<Output> {
        return transactionRepository.findById(input.id)
            .map { transaction ->
                Output(
                    id = transaction.id,
                    description = transaction.description,
                    amount = transaction.amount,
                    currency = transaction.currency,
                    totalAmount = transaction.totalAmount
                )
            }
            .switchIfEmpty { Mono.error(TransactionNotFoundException()) }
    }

    data class Input(val id: String)
    data class Output(
        val id: String,
        val description: String,
        val amount: BigDecimal,
        val totalAmount: BigDecimal,
        val currency: Currency,
    )
}