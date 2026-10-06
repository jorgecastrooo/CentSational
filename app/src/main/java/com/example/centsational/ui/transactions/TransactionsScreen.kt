package com.example.centsational.ui.transactions

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.centsational.domain.model.Transaction
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import com.example.centsational.domain.model.TransactionType
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment

@Composable
fun TransactionsScreen(modifier: Modifier = Modifier, viewModel: TransactionsViewModel = hiltViewModel())
{
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()

    LazyColumn(modifier = modifier)
    {
        item {
            Row {
                Button(onClick = { viewModel.addTestExpense() }) {
                    Text("Café")
                }
                Button(onClick = { viewModel.addTestIncome() }) {
                    Text("Salário")
                }
            }
        }

        items(transactions) { transaction ->
            val isExpense = transaction.type == TransactionType.EXPENSE
            val color = if (isExpense) Color(0xFFE53935) else Color(0xFF43A047)
            val sign = if (isExpense) "-" else "+"

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${transaction.note}: $sign${formatCents(transaction.amountCents)}",
                    color = color
                )
                IconButton(onClick = { viewModel.deleteTransaction(transaction) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Apagar")
                }
            }
        }
    }
}

private fun formatCents(cents: Long): String
{
    return NumberFormat.getCurrencyInstance(Locale("pt", "PT")).format(cents / 100.0)
}