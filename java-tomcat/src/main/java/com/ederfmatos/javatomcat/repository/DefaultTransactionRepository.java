package com.ederfmatos.javatomcat.repository;

import com.ederfmatos.javatomcat.entity.Transaction;
import com.ederfmatos.javatomcat.enums.Currency;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DefaultTransactionRepository implements TransactionRepository {
    private final JdbcClient jdbcClient;

    @Override
    public void create(Transaction transaction) {
        var sql = """
            INSERT INTO transactions (id, description, amount, total_amount, currency, created_at)
            VALUES (:id, :description, :amount, :totalAmount, :currency, :createdAt)
        """;
        jdbcClient.sql(sql)
                .param("id", transaction.id())
                .param("description", transaction.description())
                .param("amount", transaction.amount())
                .param("totalAmount", transaction.totalAmount())
                .param("currency", transaction.currency().name())
                .param("createdAt", Date.from(transaction.createdAt().toInstant()))
                .update();
    }

    @Override
    public Optional<Transaction> findById(String id) {
        var sql = """
            SELECT id, description, amount, total_amount, currency, created_at
            FROM transactions
            WHERE id = :id
        """;
        return jdbcClient.sql(sql)
                .param("id", id)
                .query((rs, rowNum) -> new Transaction(
                        rs.getString("id"),
                        rs.getString("description"),
                        rs.getBigDecimal("amount"),
                        rs.getBigDecimal("total_amount"),
                        Currency.valueOf(rs.getString("currency")),
                        rs.getObject("created_at", Timestamp.class).toInstant().atZone(ZoneOffset.systemDefault())
                )).optional();
    }
}
