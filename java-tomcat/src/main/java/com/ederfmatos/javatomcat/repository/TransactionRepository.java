package com.ederfmatos.javatomcat.repository;

import com.ederfmatos.javatomcat.entity.Transaction;
import java.util.Optional;

public interface TransactionRepository {
    void create(Transaction transaction);

    Optional<Transaction> findById(String id);
}
