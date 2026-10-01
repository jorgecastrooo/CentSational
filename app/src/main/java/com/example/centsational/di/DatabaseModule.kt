package com.example.centsational.di

import android.content.Context
import androidx.room.Room
import com.example.centsational.data.local.AppDatabase
import com.example.centsational.data.local.dao.CategoryDao
import com.example.centsational.data.local.dao.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// imports: Room, Provides, Singleton, ApplicationContext, Context,
// InstallIn, Module, SingletonComponent, AppDatabase, TransactionDao, CategoryDao

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "centsational.db").build()
    }

    @Provides
    fun provideTransactionDao(db: AppDatabase): TransactionDao {
       return db.transactionDao()
    }

    @Provides
    fun provideCategoryDao(db: AppDatabase): CategoryDao {
        return db.categoryDao()
    }
}