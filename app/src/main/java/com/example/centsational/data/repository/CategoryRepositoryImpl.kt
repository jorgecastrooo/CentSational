package com.example.centsational.data.repository

import com.example.centsational.data.local.dao.CategoryDao
import com.example.centsational.data.mapper.toDomain
import com.example.centsational.data.mapper.toEntity
import com.example.centsational.domain.model.Category
import com.example.centsational.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val dao: CategoryDao
) : CategoryRepository {

    override fun observeAll(): Flow<List<Category>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun add(category: Category): Long =
        dao.insert(category.toEntity())
}