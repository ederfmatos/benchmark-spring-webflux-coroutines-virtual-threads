package com.ederfmatos.kotlincoroutines.controller

import com.ederfmatos.kotlincoroutines.log.Logger
import com.ederfmatos.kotlincoroutines.usecase.CreateTransaction
import com.ederfmatos.kotlincoroutines.usecase.GetTransaction
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/transactions")
class TransactionController(
    private val createTransaction: CreateTransaction,
    private val getTransaction: GetTransaction
) {
    private companion object {
        val logger = Logger.create(TransactionController::class)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun createTransaction(@RequestBody input: CreateTransaction.Input): CreateTransaction.Output {
        logger.info {
            withMessage("Request to create transaction")
            withParam("input", input)
        }
        return createTransaction.execute(input).also {
            logger.info {
                withMessage("Transaction created successful")
                withParam("transactionId", it)
            }
        }
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    suspend fun getTransaction(@PathVariable id: String): GetTransaction.Output {
        logger.info {
            withMessage("Request to get transaction")
            withParam("transactionId", id)
        }
        val input = GetTransaction.Input(id)
        return getTransaction.execute(input)
            .also {
                logger.info {
                    withMessage("Get transaction successful")
                    withParam("transaction", it)
                }
            }
    }
}