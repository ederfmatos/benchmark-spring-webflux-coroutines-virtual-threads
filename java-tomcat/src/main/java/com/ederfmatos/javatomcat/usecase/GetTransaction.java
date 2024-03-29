package com.ederfmatos.javatomcat.usecase;

import com.ederfmatos.javatomcat.entity.Transaction;
import com.ederfmatos.javatomcat.enums.Currency;
import com.ederfmatos.javatomcat.exceptions.TransactionNotFoundException;
import com.ederfmatos.javatomcat.repository.TransactionRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetTransaction {
    private final TransactionRepository transactionRepository;

    public Output execute(Input input) {
        return transactionRepository.findById(input.id)
                .map(Output::create)
                .orElseThrow(TransactionNotFoundException::new);
    }

    public record Input(String id) {
    }

    public record Output(String id, String description, BigDecimal amount, BigDecimal totalAmount, Currency currency) {
        public static Output create(Transaction transaction) {
            return new Output(
                    transaction.id(),
                    transaction.description(),
                    transaction.amount(),
                    transaction.totalAmount(),
                    transaction.currency()
            );
        }
    }
}
