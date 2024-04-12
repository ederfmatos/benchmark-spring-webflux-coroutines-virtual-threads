package com.ederfmatos.javawebflux.repository;

import com.ederfmatos.javawebflux.entity.Transaction;
import com.ederfmatos.javawebflux.enums.Currency;
import static java.math.BigDecimal.TEN;
import java.time.Duration;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(value = "repository.type", havingValue = "fixed", matchIfMissing = true)
public class FixedTransactionRepository implements TransactionRepository {
    @Override
    public Mono<Void> create(Transaction transaction) {
        return Mono.delay(Duration.ofMillis(20))
                .then();
    }

    @Override
    public Mono<Transaction> findById(String id) {
        return Mono.delay(Duration.ofMillis(20))
                .map(value -> new Transaction(id, "description", TEN, TEN, Currency.BRL, ZonedDateTime.now()));
    }
}
