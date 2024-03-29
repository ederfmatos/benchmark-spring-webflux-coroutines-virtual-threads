package com.ederfmatos.javawebflux.entity;

import com.ederfmatos.javawebflux.enums.Currency;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

public record Transaction(
        String id,
        String description,
        BigDecimal amount,
        BigDecimal totalAmount,
        Currency currency,
        ZonedDateTime createdAt
) {
    public static Transaction create(String description, BigDecimal amount, Currency currency, BigDecimal totalAmount) {
        return new Transaction(UUID.randomUUID().toString(), description, amount, totalAmount, currency, ZonedDateTime.now());
    }
}
