package com.ederfmatos.kotlintomcat.repository

import com.ederfmatos.kotlintomcat.entity.Transaction
import com.ederfmatos.kotlintomcat.enums.Currency
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date
import kotlin.jvm.optionals.getOrNull

@Repository
class DefaultTransactionRepository(private val jdbcClient: JdbcClient) : TransactionRepository {

    override fun create(transaction: Transaction) {
        val sql = """
            INSERT INTO transactions (id, description, amount, total_amount, currency, created_at)
            VALUES (:id, :description, :amount, :totalAmount, :currency, :createdAt)
        """.trimIndent()
        jdbcClient.sql(sql)
            .param("id", transaction.id)
            .param("description", transaction.description)
            .param("amount", transaction.amount)
            .param("totalAmount", transaction.totalAmount)
            .param("currency", transaction.currency.name)
            .param("createdAt", Date.from(transaction.createdAt.toInstant()))
            .update()
    }

    override fun findById(id: String): Transaction? {
        val sql = """
            SELECT id, description, amount, total_amount, currency, created_at
            FROM transactions
            WHERE id = :id
        """.trimIndent()
        return jdbcClient.sql(sql)
            .param("id", id)
            .query { rs, _ ->
                Transaction(
                    id = rs.getString("id"),
                    description = rs.getString("description"),
                    currency = Currency.valueOf(rs.getString("currency")),
                    amount = rs.getBigDecimal("amount"),
                    totalAmount = rs.getBigDecimal("total_amount"),
                    createdAt = rs.getObject("created_at", Timestamp::class.java).toInstant().atZone(ZoneOffset.systemDefault()),
                )
            }
            .optional()
            .getOrNull()
    }
}