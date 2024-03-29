package com.ederfmatos.javawebflux.usecase;

import com.ederfmatos.javawebflux.entity.Transaction;
import com.ederfmatos.javawebflux.enums.Currency;
import com.ederfmatos.javawebflux.gateway.CurrencyGateway;
import com.ederfmatos.javawebflux.repository.TransactionRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CreateTransaction {
    private final CurrencyGateway currencyGateway;
    private final TransactionRepository transactionRepository;

    public Mono<Output> execute(Input input) {
        return currencyGateway.getCurrencies()
                .map(currencies -> {
                    BigDecimal currency = currencies.get(input.currency);
                    var totalAmount = currency.multiply(input.amount);
                    return Transaction.create(input.description, input.amount, input.currency, totalAmount);
                })
                .flatMap(transaction -> transactionRepository.create(transaction).thenReturn(transaction))
                .map(transaction -> new Output(transaction.id()));
    }

    public record Input(String description, BigDecimal amount, Currency currency) {
    }

    public record Output(String id) {
    }
}
