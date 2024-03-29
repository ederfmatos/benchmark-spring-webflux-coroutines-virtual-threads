package com.ederfmatos.kotlinwebflux.usecase

import com.ederfmatos.kotlinwebflux.entity.Transaction
import com.ederfmatos.kotlinwebflux.enums.Currency
import com.ederfmatos.kotlinwebflux.gateway.CurrencyGateway
import com.ederfmatos.kotlinwebflux.repository.TransactionRepository
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.math.BigDecimal

@Component
class CreateTransaction(
    private val currencyGateway: CurrencyGateway,
    private val transactionRepository: TransactionRepository,
) {
    fun execute(input: Input): Mono<Output> {
        return currencyGateway.getCurrencies()
            .map { currencies ->
                val amount = currencies[input.currency]!!.multiply(input.amount)
                Transaction.create(
                    description = input.description,
                    totalAmount = amount,
                    amount = input.amount,
                    currency = input.currency,
                )
            }.flatMap { transaction -> transactionRepository.create(transaction).thenReturn(transaction) }
            .map { transaction -> Output(transaction.id) }
    }

    data class Input(val description: String, val amount: BigDecimal, val currency: Currency)
    data class Output(val id: String)
}