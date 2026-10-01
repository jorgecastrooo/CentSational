package com.example.centsational.domain.model

import java.time.LocalDate

data class Transaction(
    val id: Long = 0,
    val amountCents: Long,
    val type: TransactionType,
    val categoryId: Long? = null,
    val date: LocalDate,
    val note: String = "",
    val merchantNif: String? = null
)