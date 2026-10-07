package com.example.centsational.ui.transactions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.centsational.domain.model.Category
import com.example.centsational.domain.model.Transaction
import com.example.centsational.domain.model.TransactionType
import com.example.centsational.domain.repository.CategoryRepository
import com.example.centsational.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(private val transactionRepository: TransactionRepository, categoryRepository: CategoryRepository, savedStateHandle: SavedStateHandle) : ViewModel()
{
    val categories: StateFlow<List<Category>> = categoryRepository.observeAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    private val transactionId: Long = savedStateHandle.get<Long>("transactionId") ?: -1L
    val isEditing: Boolean = transactionId != -1L

    val transaction = MutableStateFlow<Transaction?>(null)

    init {
        if (isEditing)
        {
            viewModelScope.launch {
                transaction.value = transactionRepository.getById(transactionId)
            }
        }
    }

    fun save(note: String, amountCents: Long, type: TransactionType, categoryId: Long?, onSaved: () -> Unit)
    {
        viewModelScope.launch {
            if (isEditing)
            {
                val current = transaction.value ?: return@launch
                transactionRepository.update(
                    current.copy(
                        note = note,
                        amountCents = amountCents,
                        type = type,
                        categoryId = categoryId
                    )
                )
            } else {
                transactionRepository.add(
                    Transaction(
                        note = note,
                        amountCents = amountCents,
                        type = type,
                        categoryId = categoryId,
                        date = LocalDate.now()
                    )
                )
            }
            onSaved()
        }
    }
}