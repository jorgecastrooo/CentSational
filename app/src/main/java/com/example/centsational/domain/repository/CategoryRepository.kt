package com.example.centsational.domain.repository

import com.example.centsational.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun observeAll(): Flow<List<Category>>
    suspend fun add(category: Category): Long
}