package com.ederfmatos.kotlincoroutines.usecase

import com.ederfmatos.kotlincoroutines.enums.Currency
import com.ederfmatos.kotlincoroutines.exception.TransactionNotFoundException
import com.ederfmatos.kotlincoroutines.repository.TransactionRepository
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class GetTransaction(private val transactionRepository: TransactionRepository) {

    suspend fun execute(input: Input): Output {
        val transaction = transactionRepository.findById(input.id) ?: throw TransactionNotFoundException()
        return Output(
            id = transaction.id,
            description = transaction.description,
            amount = transaction.amount,
            currency = transaction.currency,
            totalAmount = transaction.totalAmount
        )
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