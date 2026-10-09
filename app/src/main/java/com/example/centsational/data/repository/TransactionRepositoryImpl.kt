package com.example.centsational.data.repository

import com.example.centsational.data.local.dao.CategoryDao
import com.example.centsational.data.local.dao.TransactionDao
import com.example.centsational.data.mapper.toDomain
import com.example.centsational.data.mapper.toEntity
import com.example.centsational.domain.model.Transaction
import com.example.centsational.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val dao: TransactionDao) : TransactionRepository
    {
        override fun observeBetween(from: LocalDate, to: LocalDate): Flow<List<Transaction>> =
            dao.observeBetween(from.toEpochDay(), to.toEpochDay())
                .map { list -> list.map { it.toDomain() } }

        override fun observeAll(): Flow<List<Transaction>> =
            dao.observeAll().map { list -> list.map { it.toDomain() } }

        override suspend fun add(transaction: Transaction): Long =
            dao.insert(transaction.toEntity())
    
        override suspend fun update(transaction: Transaction) =
            dao.update(transaction.toEntity())

        override suspend fun delete(transaction: Transaction) =
            dao.delete(transaction.toEntity())

        override suspend fun getById(id: Long): Transaction? =
            dao.getById(id)?.toDomain()

        override suspend fun isCategoryInUse(categoryId: Long): Boolean =
            dao.isCategoryInUse(categoryId)

    }