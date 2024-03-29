package com.ederfmatos.kotlincoroutines.repository

import com.ederfmatos.kotlincoroutines.entity.Transaction

interface TransactionRepository {
    suspend fun create(transaction: Transaction)
    suspend fun findById(id: String): Transaction?
}
