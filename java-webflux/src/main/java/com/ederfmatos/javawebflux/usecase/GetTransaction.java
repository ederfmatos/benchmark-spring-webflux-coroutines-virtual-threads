package com.ederfmatos.javawebflux.usecase;

import com.ederfmatos.javawebflux.entity.Transaction;
import com.ederfmatos.javawebflux.enums.Currency;
import com.ederfmatos.javawebflux.exceptions.TransactionNotFoundException;
import com.ederfmatos.javawebflux.repository.TransactionRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GetTransaction {
    private final TransactionRepository transactionRepository;

    public Mono<Output> execute(Input input) {
        return transactionRepository.findById(input.id)
                .map(Output::create)
                .switchIfEmpty(Mono.error(TransactionNotFoundException::new));
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
