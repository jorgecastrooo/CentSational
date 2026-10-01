package com.example.centsational.ui.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.centsational.domain.model.Transaction
import com.example.centsational.domain.model.TransactionType
import com.example.centsational.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val repo: TransactionRepository
) : ViewModel() {

    /**
     * Converts the Flow from the repository into a StateFlow that the UI can observe.
     * WhileSubscribed(5_000) keeps the upstream flow active for 5 seconds after the
     * last collector disappears, handling configuration changes (like rotation) efficiently.
     */
    val transactions: StateFlow<List<Transaction>> = repo.observeAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    /**
     * Launches a coroutine in the viewModelScope to add a dummy expense.
     */
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
}