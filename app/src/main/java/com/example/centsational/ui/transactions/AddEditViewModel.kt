package com.example.centsational.ui.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.centsational.domain.model.Category
import com.example.centsational.domain.model.Transaction
import com.example.centsational.domain.model.TransactionType
import com.example.centsational.domain.repository.CategoryRepository
import com.example.centsational.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(private val transactionRepository: TransactionRepository, categoryRepository: CategoryRepository) : ViewModel()
{
    val categories: StateFlow<List<Category>> = categoryRepository.observeAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun save(note: String, amountCents: Long, type: TransactionType, categoryId: Long?, onSaved: () -> Unit)
    {
        viewModelScope.launch {
            transactionRepository.add(
                Transaction(
                    note = note,
                    amountCents = amountCents,
                    type = type,
                    categoryId = categoryId,
                    date = LocalDate.now()
                )
            )
            onSaved()
        }
    }
}