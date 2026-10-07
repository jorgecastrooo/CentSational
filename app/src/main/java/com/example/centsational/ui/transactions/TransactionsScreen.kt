package com.example.centsational.ui.transactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.centsational.domain.model.TransactionType
import com.example.centsational.ui.theme.green
import com.example.centsational.ui.theme.grey
import com.example.centsational.ui.theme.red
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MenuAnchorType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TransactionsScreen(modifier: Modifier = Modifier, viewModel: TransactionsViewModel = hiltViewModel())
{
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val balanceCents by viewModel.balanceCents.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()

    var note by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(TransactionType.EXPENSE) }
    var selectedCategoryId  by remember { mutableStateOf<Long?>(null) }
    var expanded by remember { mutableStateOf(false) }
    val selectedName = categories.find { it.id == selectedCategoryId }?.name ?: ""

    val amountCents: Long? = amountText.replace(',', '.').toBigDecimalOrNull() ?.setScale(2, RoundingMode.HALF_UP) ?.movePointRight(2) ?.toLong()

    LazyColumn(modifier = modifier) {
        item {
            val balanceColor = when {
                balanceCents < 0 -> red
                balanceCents > 0 -> green
                else -> grey
            }

            Column {
                Text(text = "Saldo: ${formatCents(balanceCents)}", color = balanceColor)

                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    SegmentedButton(selected = type == TransactionType.EXPENSE, onClick = { type = TransactionType.EXPENSE }, shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2))
                    {
                        Text("Despesa")
                    }

                    SegmentedButton(selected = type == TransactionType.INCOME, onClick = { type = TransactionType.INCOME }, shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2))
                    {
                        Text("Receita")
                    }
                }

                OutlinedTextField(value = note, onValueChange = { note = it }, label = { Text("Nota") })

                OutlinedTextField(value = amountText, onValueChange = { amountText = it }, label = { Text("Valor (€)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))


                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it })
                {
                    OutlinedTextField(
                        value = selectedName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoria") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false })
                    {
                        DropdownMenuItem(text = { Text("Sem categoria") },
                            onClick = {
                                selectedCategoryId = null
                                expanded = false
                            }
                        )
                        categories.forEach { category ->
                            DropdownMenuItem(text = { Text(category.name) },
                                onClick = {
                                    selectedCategoryId = category.id
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Button(enabled = amountCents != null && amountCents > 0 && note.isNotBlank() && selectedCategoryId  != null,
                    onClick = {
                        viewModel.addTransaction(note, amountCents!!, type, selectedCategoryId )
                        note = ""
                        amountText = ""
                        selectedCategoryId  = null
                    }
                ) {
                    Text("Guardar")
                }
            }
        }

        items(transactions) { transaction ->
            val isExpense = transaction.type == TransactionType.EXPENSE
            val color = if (isExpense) red else green
            val sign = if (isExpense) "-" else "+"
            val categoryName = categories.find { it.id == transaction.categoryId }?.name ?: "Sem categoria"

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically)
            {
                Text(text = "${transaction.note} ($categoryName): $sign${formatCents(transaction.amountCents)}", color = color)

                IconButton(onClick = { viewModel.deleteTransaction(transaction) })
                {
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