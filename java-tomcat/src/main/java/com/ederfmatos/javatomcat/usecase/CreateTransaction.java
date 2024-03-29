package com.ederfmatos.javatomcat.usecase;

import com.ederfmatos.javatomcat.entity.Transaction;
import com.ederfmatos.javatomcat.enums.Currency;
import com.ederfmatos.javatomcat.gateway.CurrencyGateway;
import com.ederfmatos.javatomcat.repository.TransactionRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateTransaction {
    private final CurrencyGateway currencyGateway;
    private final TransactionRepository transactionRepository;

    public Output execute(Input input) {
        var currencies = currencyGateway.getCurrencies();
        BigDecimal currency = currencies.get(input.currency);
        var totalAmount = currency.multiply(input.amount);
        var transaction = Transaction.create(input.description, input.amount, input.currency, totalAmount);
        transactionRepository.create(transaction);
        return new Output(transaction.id());
    }

    public record Input(String description, BigDecimal amount, Currency currency) {
    }

    public record Output(String id) {
    }
}
