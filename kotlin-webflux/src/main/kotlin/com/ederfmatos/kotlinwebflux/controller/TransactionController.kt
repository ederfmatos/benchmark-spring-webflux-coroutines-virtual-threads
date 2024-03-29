package com.ederfmatos.kotlinwebflux.controller

import com.ederfmatos.kotlinwebflux.log.Logger
import com.ederfmatos.kotlinwebflux.usecase.CreateTransaction
import com.ederfmatos.kotlinwebflux.usecase.GetTransaction
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/transactions")
class TransactionController(
    private val createTransaction: CreateTransaction,
    private val getTransaction: GetTransaction,
) {
    private companion object {
        val logger = Logger.create(TransactionController::class)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createTransaction(@RequestBody input: CreateTransaction.Input): Mono<CreateTransaction.Output> {
        return Mono.just(input)
            .doOnNext {
                logger.info {
                    withMessage("Request to create transaction")
                    withParam("input", it)
                }
            }.flatMap(createTransaction::execute)
            .doOnSuccess {
                logger.info {
                    withMessage("Transaction created successful")
                    withParam("transactionId", it)
                }
            }
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getTransaction(@PathVariable id: String): Mono<GetTransaction.Output> {
        return Mono.just(GetTransaction.Input(id))
            .doOnNext {
                logger.info {
                    withMessage("Request to get transaction")
                    withParam("transactionId", it.id)
                }
            }
            .flatMap(getTransaction::execute)
            .doOnSuccess {
                logger.info {
                    withMessage("Get transaction successful")
                    withParam("transaction", it)
                }
            }
    }
}