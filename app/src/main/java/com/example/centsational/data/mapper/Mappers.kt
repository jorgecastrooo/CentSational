package com.example.centsational.data.mapper

import com.example.centsational.data.local.entity.CategoryEntity
import com.example.centsational.data.local.entity.TransactionEntity
import com.example.centsational.domain.model.Category
import com.example.centsational.domain.model.Transaction
import com.example.centsational.domain.model.TransactionType
import java.time.LocalDate

fun CategoryEntity.toDomain() = Category(
    id = id,
    name = name,
    icon = icon,
    colorHex = colorHex,
    type = TransactionType.valueOf(type)
)

fun Category.toEntity() = CategoryEntity(
    id = id,
    name = name,
    icon = icon,
    colorHex = colorHex,
    type = type.name
)

fun TransactionEntity.toDomain() = Transaction(
    id = id,
    amountCents = amountCents,
    type = TransactionType.valueOf(type),
    categoryId = categoryId,
    date = LocalDate.ofEpochDay(dateEpochDay),
    note = note,
    merchantNif = merchantNif
)

fun Transaction.toEntity() = TransactionEntity(
    id = id,
    amountCents = amountCents,
    type = type.name,
    categoryId = categoryId,
    dateEpochDay = date.toEpochDay(),
    note = note,
    merchantNif = merchantNif
)
    