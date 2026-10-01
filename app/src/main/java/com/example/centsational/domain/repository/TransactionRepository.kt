package com.example.centsational.domain.repository

import com.example.centsational.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TransactionRepository {
    fun observeAll(): Flow<List<Transaction>>
    fun observeBetween(from: LocalDate, to: LocalDate): Flow<List<Transaction>>
    suspend fun add(transaction: Transaction): Long
    suspend fun update(transaction: Transaction)
    suspend fun delete(transaction: Transaction)
}