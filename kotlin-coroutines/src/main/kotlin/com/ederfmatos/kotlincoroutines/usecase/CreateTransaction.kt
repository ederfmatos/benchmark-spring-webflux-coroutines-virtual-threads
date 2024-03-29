package com.ederfmatos.kotlincoroutines.usecase

import com.ederfmatos.kotlincoroutines.entity.Transaction
import com.ederfmatos.kotlincoroutines.enums.Currency
import com.ederfmatos.kotlincoroutines.gateway.CurrencyGateway
import com.ederfmatos.kotlincoroutines.repository.TransactionRepository
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class CreateTransaction(
    private val currencyGateway: CurrencyGateway,
    private val transactionRepository: TransactionRepository
) {

    suspend fun execute(input: Input): Output {
        val currencies = currencyGateway.getCurrencies()
        val amount = currencies[input.currency]!!.multiply(input.amount)
        val transaction = Transaction.create(
            description = input.description,
            totalAmount = amount,
            amount = input.amount,
            currency = input.currency,
        )
        transactionRepository.create(transaction)
        return Output(transaction.id)
    }

    data class Input(val description: String, val amount: BigDecimal, val currency: Currency)
    data class Output(val id: String)
}