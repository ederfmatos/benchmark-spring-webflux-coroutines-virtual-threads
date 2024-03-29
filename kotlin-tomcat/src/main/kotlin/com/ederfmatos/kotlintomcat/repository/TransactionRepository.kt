package com.ederfmatos.kotlintomcat.repository

import com.ederfmatos.kotlintomcat.entity.Transaction

interface TransactionRepository {
    fun create(transaction: Transaction)
    fun findById(id: String): Transaction?
}
