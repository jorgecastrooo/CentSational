package com.example.centsational.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.centsational.domain.model.Category
import com.example.centsational.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(private val categoryRepository: CategoryRepository) : ViewModel()
{
    val categories: StateFlow<List<Category>> = categoryRepository.observeAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun add(category: Category)
    {
        viewModelScope.launch {
            categoryRepository.add(category)
        }
    }

    fun update(category: Category)
    {
        viewModelScope.launch {
            categoryRepository.update(category)
        }
    }

    fun delete(category: Category)
    {
        viewModelScope.launch {
            categoryRepository.delete(category)
        }
    }
}