package com.example.centsational.ui.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.centsational.domain.model.Category
import com.example.centsational.domain.model.TransactionType
import com.example.centsational.ui.components.CategoryBadge
import com.example.centsational.ui.components.ColorPicker
import com.example.centsational.ui.components.IconPicker
import com.example.centsational.ui.components.categoryColors
import com.example.centsational.ui.components.categoryIcons
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryFormSheet(category: Category?, initialType: TransactionType,
    onSave: (name: String, icon: String, colorHex: Long, type: TransactionType) -> Unit, onDismiss: () -> Unit)
{
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val type = category?.type ?: initialType

    var name by remember(category) { mutableStateOf(category?.name ?: "") }
    var selectedColor by remember(category) { mutableStateOf(category?.colorHex ?: categoryColors.first()) }
    var selectedIcon by remember(category) { mutableStateOf(category?.icon ?: categoryIcons.keys.first()) }

    val typeLabel = if (type == TransactionType.EXPENSE) "de despesa" else "de receita"

    val close: () -> Unit = {
        scope.launch { sheetState.hide() }.invokeOnCompletion { onDismiss() }
    }

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState)
    {
        Column(modifier = Modifier.verticalScroll(rememberScrollState()).imePadding().padding(horizontal = 16.dp)
            .padding(bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(16.dp))
        {
            Text(text = if (category != null) "Editar categoria $typeLabel" else "Nova categoria $typeLabel", style = MaterialTheme.typography.titleLarge)

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp))
            {
                CategoryBadge(icon = selectedIcon, colorHex = selectedColor, size = 56.dp)
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nome") },
                    singleLine = true, modifier = Modifier.weight(1f)
                )
            }

            Text("Cor", style = MaterialTheme.typography.titleMedium)
            ColorPicker(colors = categoryColors, selected = selectedColor, onSelect = { selectedColor = it })

            Text("Ícone", style = MaterialTheme.typography.titleMedium)
            IconPicker(icons = categoryIcons, selected = selectedIcon, selectedColor = selectedColor,
                onSelect = { selectedIcon = it }
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp))
            {
                Button(enabled = name.isNotBlank(),
                    onClick = {
                        onSave(name.trim(), selectedIcon, selectedColor, type)
                        close()
                    }
                ) {
                    Text(if (category != null) "Atualizar" else "Guardar")
                }
                TextButton(onClick = close) {
                    Text("Cancelar")
                }
            }
        }
    }
}