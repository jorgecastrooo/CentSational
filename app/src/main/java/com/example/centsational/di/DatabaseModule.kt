package com.example.centsational.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.centsational.data.local.AppDatabase
import com.example.centsational.data.local.dao.CategoryDao
import com.example.centsational.data.local.dao.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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
                        // despesas
                        SeedCategory("Alimentação", "restaurant", 0xFFE57373L, "EXPENSE"),
                        SeedCategory("Transportes", "directions_car", 0xFF64B5F6L, "EXPENSE"),
                        SeedCategory("Casa", "home", 0xFF81C784L, "EXPENSE"),
                        SeedCategory("Lazer", "movie", 0xFFBA68C8L, "EXPENSE"),
                        SeedCategory("Saúde", "favorite", 0xFFFFB74DL, "EXPENSE"),
                        SeedCategory("Outros", "more_horiz", 0xFF90A4AEL, "EXPENSE"),
                        // receitas
                        SeedCategory("Salário", "payments", 0xFF66BB6AL, "INCOME"),
                        SeedCategory("Extras", "attach_money", 0xFF26A69AL, "INCOME"),
                        SeedCategory("Investimentos", "trending_up", 0xFF42A5F5L, "INCOME"),
                        SeedCategory("Presentes", "card_giftcard", 0xFFBA68C8L, "INCOME"),
                        SeedCategory("Outros", "more_horiz", 0xFF90A4AEL, "INCOME")
                    )
                    defaults.forEach {
                        db.execSQL(
                            "INSERT INTO categories (name, icon, colorHex, type) " +
                                    "VALUES ('${it.name}', '${it.icon}', ${it.color}, '${it.type}')"
                        )
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

private data class SeedCategory(
    val name: String,
    val icon: String,
    val color: Long,
    val type: String
)