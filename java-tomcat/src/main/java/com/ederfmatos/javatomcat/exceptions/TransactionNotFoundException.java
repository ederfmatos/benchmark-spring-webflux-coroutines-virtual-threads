package com.ederfmatos.javatomcat.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class TransactionNotFoundException extends HttpClientErrorException {
    public TransactionNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Transaction not found");
    }
}
