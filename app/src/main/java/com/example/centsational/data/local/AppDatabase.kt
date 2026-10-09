package com.example.centsational.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.centsational.data.local.dao.CategoryDao
import com.example.centsational.data.local.dao.TransactionDao
import com.example.centsational.data.local.entity.CategoryEntity
import com.example.centsational.data.local.entity.TransactionEntity

@Database(
    entities = [TransactionEntity::class, CategoryEntity::class],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
}