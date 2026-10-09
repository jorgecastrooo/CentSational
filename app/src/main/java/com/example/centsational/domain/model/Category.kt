package com.example.centsational.domain.model

data class Category(
    val id: Long = 0,
    val name: String,
    val icon: String,
    val colorHex: Long,
    val type: TransactionType
)