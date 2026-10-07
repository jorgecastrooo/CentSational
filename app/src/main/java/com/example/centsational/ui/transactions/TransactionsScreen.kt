package com.example.centsational.ui.transactions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.centsational.domain.model.TransactionType
import com.example.centsational.ui.theme.green
import com.example.centsational.ui.theme.grey
import com.example.centsational.ui.theme.red
import java.text.NumberFormat
import java.util.Locale

@Composable
fun TransactionsScreen(modifier: Modifier = Modifier, onAdd: () -> Unit, onEdit : (Long) -> Unit , viewModel: TransactionsViewModel = hiltViewModel())
{
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val balanceCents by viewModel.balanceCents.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()

    val balanceColor = when {
        balanceCents < 0 -> red
        balanceCents > 0 -> green
        else -> grey
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Text(text = "Saldo: ${formatCents(balanceCents)}", color = balanceColor, modifier = Modifier.padding(16.dp))
            }

            items(transactions) { transaction ->
                val isExpense = transaction.type == TransactionType.EXPENSE
                val color = if (isExpense) red else green
                val sign = if (isExpense) "-" else "+"
                val categoryName = categories.find { it.id == transaction.categoryId }?.name ?: "Sem categoria"

                Row(modifier = Modifier.fillMaxWidth()
                    .clickable {
                        onEdit(transaction.id)
                    }
                    .padding(horizontal = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically)
                {
                    Text(text = "${transaction.note} ($categoryName): $sign${formatCents(transaction.amountCents)}", color = color)

                    IconButton(onClick = { viewModel.deleteTransaction(transaction) }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Apagar")
                    }
                }
            }
        }

        FloatingActionButton(onClick = onAdd, modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp))
        {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Nova transação")
        }
    }
}

private fun formatCents(cents: Long): String
{
    return NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-PT")).format(cents / 100.0)
}