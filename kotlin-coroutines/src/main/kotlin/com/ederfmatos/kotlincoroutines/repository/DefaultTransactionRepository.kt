package com.ederfmatos.kotlincoroutines.repository

import com.ederfmatos.kotlincoroutines.entity.Transaction
import com.ederfmatos.kotlincoroutines.enums.Currency
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneOffset

@Repository
@ConditionalOnProperty(value = ["repository.type"], havingValue = "r2dbc")
class DefaultTransactionRepository(private val databaseClient: DatabaseClient) : TransactionRepository {

    override suspend fun create(transaction: Transaction) {
        val sql = """
            INSERT INTO transactions (id, description, amount, total_amount, currency, created_at)
            VALUES (:id, :description, :amount, :totalAmount, :currency, :createdAt)
        """.trimIndent()
        databaseClient.sql(sql)
            .bind("id", transaction.id)
            .bind("description", transaction.description)
            .bind("amount", transaction.amount)
            .bind("totalAmount", transaction.totalAmount)
            .bind("currency", transaction.currency.name)
            .bind("createdAt", transaction.createdAt)
            .then()
            .awaitSingleOrNull()
    }

    override suspend fun findById(id: String): Transaction? {
        val sql = """
            SELECT id, description, amount, total_amount, currency, created_at
            FROM transactions
            WHERE id = :id
        """.trimIndent()
        return databaseClient.sql(sql)
            .bind("id", id)
            .fetch()
            .one()
            .awaitSingle()
            .let { row ->
                Transaction(
                    id = row["id"].toString(),
                    description = row["description"].toString(),
                    currency = Currency.valueOf(row["currency"].toString()),
                    amount = row["amount"] as BigDecimal,
                    totalAmount = row["total_amount"] as BigDecimal,
                    createdAt = (row["created_at"] as LocalDateTime).atZone(ZoneOffset.systemDefault()),
                )
            }
    }
}