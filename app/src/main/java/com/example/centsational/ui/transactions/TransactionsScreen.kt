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

@Composable
fun TransactionsScreen(
    viewModel: TransactionsViewModel = hiltViewModel()
) {
    // Observa o StateFlow da ViewModel de forma segura para o ciclo de vida
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()

    // Exemplo simples de UI
    LazyColumn {
        item {
            Button(onClick = { viewModel.addTestExpense() }) {
                Text("Adicionar Café (Teste)")
            }
        }
        items(transactions) { transaction ->
            Text(text = "${transaction.note}: ${transaction.amountCents / 100.0}€")
        }
    }
}