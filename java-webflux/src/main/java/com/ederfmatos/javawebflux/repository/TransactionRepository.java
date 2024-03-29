package com.ederfmatos.javawebflux.repository;

import com.ederfmatos.javawebflux.entity.Transaction;
import reactor.core.publisher.Mono;

public interface TransactionRepository {
    Mono<Void> create(Transaction transaction);
    Mono<Transaction> findById(String id);
}
