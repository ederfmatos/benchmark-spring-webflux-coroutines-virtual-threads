package com.ederfmatos.javawebflux.repository;

import com.ederfmatos.javawebflux.entity.Transaction;
import com.ederfmatos.javawebflux.enums.Currency;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class DefaultTransactionRepository implements TransactionRepository {
    private final DatabaseClient databaseClient;

    @Override
    public Mono<Void> create(Transaction transaction) {
        var sql = """
            INSERT INTO transactions (id, description, amount, total_amount, currency, created_at)
            VALUES (:id, :description, :amount, :totalAmount, :currency, :createdAt)
        """;
        return databaseClient.sql(sql)
                .bind("id", transaction.id())
                .bind("description", transaction.description())
                .bind("amount", transaction.amount())
                .bind("totalAmount", transaction.totalAmount())
                .bind("currency", transaction.currency().name())
                .bind("createdAt", Date.from(transaction.createdAt().toInstant()))
                .then();
    }

    @Override
    public Mono<Transaction> findById(String id) {
        var sql = """
            SELECT id, description, amount, total_amount, currency, created_at
            FROM transactions
            WHERE id = :id
        """;
        return databaseClient.sql(sql)
                .bind("id", id)
                .fetch()
                .one()
                .map(row -> new Transaction(
                        row.get("id").toString(),
                        row.get("description").toString(),
                        (BigDecimal) row.get("amount"),
                        (BigDecimal) row.get("total_amount"),
                        Currency.valueOf(row.get("currency").toString()),
                        ((LocalDateTime) row.get("created_at")).atZone(ZoneOffset.systemDefault())
                ));
    }
}
