package com.example.centsational.ui.transactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.centsational.domain.model.TransactionType
import java.math.BigDecimal
import java.math.RoundingMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(modifier: Modifier = Modifier, onDone: () -> Unit, viewModel: AddEditViewModel = hiltViewModel())
{
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val transaction by viewModel.transaction.collectAsStateWithLifecycle()

    var note by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(TransactionType.EXPENSE) }
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }
    var expanded by remember { mutableStateOf(false) }

    val selectedName = categories.find { it.id == selectedCategoryId }?.name ?: ""
    val amountCents: Long? = amountText.replace(',', '.').toBigDecimalOrNull() ?.setScale(2, RoundingMode.HALF_UP) ?.movePointRight(2) ?.toLong()
    val canSave = amountCents != null && amountCents > 0 && note.isNotBlank() && selectedCategoryId != null

    LaunchedEffect(transaction) { transaction?.let {
        note = it.note
        amountText = BigDecimal(it.amountCents).movePointLeft(2).toPlainString().replace('.', ',')
        type = it.type
        selectedCategoryId = it.categoryId
    }

    }

    Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp))
    {
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            SegmentedButton(selected = type == TransactionType.EXPENSE, onClick = { type = TransactionType.EXPENSE },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
            ) {
                Text("Despesa")
            }
            SegmentedButton(selected = type == TransactionType.INCOME, onClick = { type = TransactionType.INCOME },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
            ) {
                Text("Receita")
            }
        }

        OutlinedTextField(value = note, onValueChange = { note = it }, label = { Text("Nota") }, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(value = amountText, onValueChange = { amountText = it }, label = { Text("Valor (€)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.fillMaxWidth())

        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it })
        {
            OutlinedTextField(value = selectedName, onValueChange = {}, readOnly = true, label = { Text("Categoria") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable)
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false })
            {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = {
                            selectedCategoryId = category.id
                            expanded = false
                        }
                    )
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                enabled = canSave,
                onClick = {
                    viewModel.save(note, amountCents!!, type, selectedCategoryId, onSaved = onDone)
                }
            ) {
                Text(if (viewModel.isEditing) "Atualizar" else "Guardar")
               }
            TextButton(onClick = onDone) {
                Text("Cancelar")
            }
        }
    }
}