package com.ederfmatos.javatomcat.repository;

import com.ederfmatos.javatomcat.entity.Transaction;
import com.ederfmatos.javatomcat.enums.Currency;
import java.math.BigDecimal;
import static java.math.BigDecimal.TEN;
import java.time.ZonedDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(value = "repository.type", havingValue = "fixed", matchIfMissing = true)
public class FixedTransactionRepository implements TransactionRepository {

    @Override
    @SneakyThrows
    public void create(Transaction transaction) {
        Thread.sleep(20);
    }

    @Override
    @SneakyThrows
    public Optional<Transaction> findById(String id) {
        Thread.sleep(20);
        var transaction = new Transaction(id, "description", TEN, TEN, Currency.BRL, ZonedDateTime.now());
        return Optional.of(transaction);
    }
}
