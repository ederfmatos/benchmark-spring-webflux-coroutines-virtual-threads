package com.ederfmatos.javatomcat.controller;

import com.ederfmatos.javatomcat.log.Logger;
import com.ederfmatos.javatomcat.usecase.CreateTransaction;
import com.ederfmatos.javatomcat.usecase.GetTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {
    private static final Logger logger = Logger.create(TransactionController.class);
    private final CreateTransaction createTransaction;
    private final GetTransaction getTransaction;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateTransaction.Output createTransaction(@RequestBody CreateTransaction.Input input) {
        logger.info(log -> log.withMessage("Request to create transaction").withParam("input", input));
        var output = createTransaction.execute(input);
        logger.info(log -> log.withMessage("Transaction created successful").withParam("transactionId", output));
        return output;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GetTransaction.Output getTransaction(@PathVariable String id) {
        logger.info(log -> log.withMessage("Request to get transaction").withParam("transactionId", id));
        var input = new GetTransaction.Input(id);
        var output = getTransaction.execute(input);
        logger.info(log -> log.withMessage("Get transaction successful").withParam("transaction", output));
        return output;
    }

}
