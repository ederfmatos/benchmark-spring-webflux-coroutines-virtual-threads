package com.ederfmatos.javawebflux.controller;

import com.ederfmatos.javawebflux.log.Logger;
import com.ederfmatos.javawebflux.usecase.CreateTransaction;
import com.ederfmatos.javawebflux.usecase.GetTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {
    private static final Logger logger = Logger.create(TransactionController.class);
    private final CreateTransaction createTransaction;
    private final GetTransaction getTransaction;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CreateTransaction.Output> createTransaction(@RequestBody CreateTransaction.Input input) {
        return Mono.just(input)
                .doOnNext(it -> logger.info(log -> log.withMessage("Request to create transaction").withParam("input", it)))
                .flatMap(createTransaction::execute)
                .doOnSuccess(output -> logger.info(log -> log.withMessage("Transaction created successful").withParam("transactionId", output)));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<GetTransaction.Output> getTransaction(@PathVariable String id) {
        return Mono.just(new GetTransaction.Input(id))
                .doOnNext(it -> logger.info(log -> log.withMessage("Request to get transaction").withParam("transactionId", it.id())))
                .flatMap(getTransaction::execute)
                .doOnSuccess(output -> logger.info(log -> log.withMessage("Get transaction successful").withParam("transaction", output)));
    }
}