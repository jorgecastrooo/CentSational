package com.example.centsational.ui.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.centsational.domain.model.Transaction
import com.example.centsational.domain.model.TransactionType
import com.example.centsational.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.map

@HiltViewModel
class TransactionsViewModel @Inject constructor(private val repo: TransactionRepository) : ViewModel()
{
    val transactions: StateFlow<List<Transaction>> = repo.observeAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    val balanceCents: StateFlow<Long> = transactions.map {
        list ->
            list.sumOf {
                if(it.type == TransactionType.EXPENSE)
                    -it.amountCents
                else
                    it.amountCents.toLong() }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0L
        )

    fun addTestExpense() {
        viewModelScope.launch {
            repo.add(
                Transaction(
                    amountCents = 120,
                    type = TransactionType.EXPENSE,
                    date = LocalDate.now(),
                    note = "Café"
                )
            )
        }
    }

    fun addTestIncome(){
        viewModelScope.launch {
            repo.add(
                Transaction(
                    amountCents = 100000,
                    type = TransactionType.INCOME,
                    date = LocalDate.now(),
                    note = "Salário"
                )
            )
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repo.delete(transaction)
        }
    }

}
