package com.example.centsational.di

import com.example.centsational.data.repository.CategoryRepositoryImpl
import com.example.centsational.data.repository.TransactionRepositoryImpl
import com.example.centsational.domain.repository.CategoryRepository
import com.example.centsational.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindTransactionRepository(impl: TransactionRepositoryImpl): TransactionRepository

    @Binds
    abstract fun bindCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository
}