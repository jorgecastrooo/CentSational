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
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "centsational.db")
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    val defaults = listOf(
                        Triple("Alimentação", "restaurant", 0xFFE57373L),
                        Triple("Transportes", "directions_car", 0xFF64B5F6L),
                        Triple("Casa", "home", 0xFF81C784L),
                        Triple("Lazer", "movie", 0xFFBA68C8L),
                        Triple("Saúde", "favorite", 0xFFFFB74DL),
                        Triple("Outros", "more_horiz", 0xFF90A4AEL)
                    )
                    defaults.forEach { (name, icon, color) ->
                        db.execSQL("INSERT INTO categories (name, icon, colorHex) VALUES ('$name', '$icon', $color)")
                    }
                }
            })
            .build()
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