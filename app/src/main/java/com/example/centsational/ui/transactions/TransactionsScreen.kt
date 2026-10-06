package com.example.centsational.ui.transactions

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

@Composable
fun TransactionsScreen(
    modifier: Modifier = Modifier,
    viewModel: TransactionsViewModel = hiltViewModel()
) {
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()

    LazyColumn(modifier = modifier) {
        item {
            Button(onClick = { viewModel.addTestExpense() }) {
                Text("Adicionar Café (Teste)")
            }
        }
        items(transactions) { transaction ->
            Text(text = "${transaction.note}: ${transaction.amountCents / 100.0}€")
            IconButton(onClick = { viewModel.deleteTransaction(transaction) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Apagar"
                )
            }
        }
    }
}