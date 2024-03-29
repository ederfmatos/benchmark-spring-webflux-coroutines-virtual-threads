package com.ederfmatos.kotlinwebflux.repository

import com.ederfmatos.kotlinwebflux.entity.Transaction
import com.ederfmatos.kotlinwebflux.enums.Currency
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneOffset

@Repository
class DefaultTransactionRepository(private val databaseClient: DatabaseClient) : TransactionRepository {

    override fun create(transaction: Transaction): Mono<Void> {
        val sql = """
            INSERT INTO transactions (id, description, amount, total_amount, currency, created_at)
            VALUES (:id, :description, :amount, :totalAmount, :currency, :createdAt)
        """.trimIndent()
        return databaseClient.sql(sql)
            .bind("id", transaction.id)
            .bind("description", transaction.description)
            .bind("amount", transaction.amount)
            .bind("totalAmount", transaction.totalAmount)
            .bind("currency", transaction.currency.name)
            .bind("createdAt", transaction.createdAt)
            .then()
            .subscribeOn(Schedulers.boundedElastic())
    }

    override fun findById(id: String): Mono<Transaction> {
        val sql = """
            SELECT id, description, amount, total_amount, currency, created_at
            FROM transactions
            WHERE id = :id
        """.trimIndent()
        return databaseClient.sql(sql)
            .bind("id", id)
            .fetch()
            .one()
            .map { row ->
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