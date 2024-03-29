package com.ederfmatos.kotlinwebflux.exception

import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException

class TransactionNotFoundException : HttpClientErrorException(HttpStatus.NOT_FOUND, "Transaction not found")