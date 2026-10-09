package com.example.centsational.ui.categories

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.*
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.centsational.domain.model.Category
import com.example.centsational.domain.model.TransactionType
import com.example.centsational.ui.components.CategoryBadge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(modifier: Modifier = Modifier, onBack: () -> Unit, viewModel: CategoriesViewModel = hiltViewModel())
{
    val categories by viewModel.categories.collectAsStateWithLifecycle()

    var type by rememberSaveable { mutableStateOf(TransactionType.EXPENSE) }
    var showSheet by remember { mutableStateOf(false) }
    var editing by remember { mutableStateOf<Category?>(null) }
    var showArchivedSheet by remember { mutableStateOf(false) }
    var headerMenuOpen by remember { mutableStateOf(false) }

    val filtered = categories.filter { it.type == type }

    Scaffold(modifier = modifier, contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        topBar = {
            TopAppBar(windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp), title = { Text("Categorias") },
                navigationIcon = {
                    IconButton(onClick = onBack)
                    {
                        Icon(imageVector = Lucide.ArrowLeft, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = { showArchivedSheet = true })
                    {
                        Icon(imageVector = Lucide.Archive, contentDescription = "Categorias arquivadas")
                    }

                    Box {
                        IconButton(onClick = { headerMenuOpen = true })
                        {
                            Icon(imageVector = Lucide.EllipsisVertical, contentDescription = "Mais opções")
                        }

                        DropdownMenu(expanded = headerMenuOpen, onDismissRequest = { headerMenuOpen = false })
                        {
                            DropdownMenuItem(
                                text = { Text("Mover transações") },
                                leadingIcon = {
                                    Icon(Lucide.ArrowLeftRight, contentDescription = null)
                                },
                                onClick = {
                                    headerMenuOpen = false
                                    // por fazer
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->

        Box(modifier = Modifier.padding(innerPadding).fillMaxSize())
        {
            Column(modifier = Modifier.fillMaxSize())
            {
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth().padding(16.dp))
                {
                    SegmentedButton(
                        selected = type == TransactionType.EXPENSE,
                        onClick = { type = TransactionType.EXPENSE },
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
                    ) {
                        Text("Despesa")
                    }
                    SegmentedButton(
                        selected = type == TransactionType.INCOME,
                        onClick = { type = TransactionType.INCOME },
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
                    ) {
                        Text("Receita")
                    }
                }

                LazyColumn(modifier = Modifier.fillMaxSize())
                {
                    items(filtered, key = { it.id }) { category ->
                        CategoryRow(category = category,
                            onEdit = {
                                editing = category
                                showSheet = true
                            }
                        )
                    }
                }
            }

            FloatingActionButton(modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
                onClick = {
                    editing = null
                    showSheet = true
                }
            ) {
                Icon(imageVector = Lucide.Plus, contentDescription = "Nova categoria")
            }
        }
    }

    if (showSheet)
    {
        CategoryFormSheet(category = editing, initialType = type,
            onSave = { name, icon, colorHex, newType ->
                val current = editing
                if (current == null)
                {
                    viewModel.add(
                        Category(
                            name = name,
                            icon = icon,
                            colorHex = colorHex,
                            type = newType
                        )
                    )
                } else {
                    viewModel.update(
                        current.copy(
                            name = name,
                            icon = icon,
                            colorHex = colorHex
                        )
                    )
                }
            },
            onDismiss = {
                showSheet = false
                editing = null
            }
        )
    }

    if (showArchivedSheet)
    {
        ModalBottomSheet(onDismissRequest = { showArchivedSheet = false })
        {
            Text(text = "Categorias arquivadas",style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 32.dp))
        }
    }
}

@Composable
private fun CategoryRow(category: Category, onEdit: () -> Unit)
{
    var menuOpen by remember { mutableStateOf(false) }

    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically)
    {
        CategoryBadge(icon = category.icon, colorHex = category.colorHex)

        Text(text = category.name, modifier = Modifier.weight(1f).padding(horizontal = 12.dp))

        Box {
            IconButton(onClick = { menuOpen = true })
            {
                Icon(imageVector = Lucide.Ellipsis, contentDescription = "Opções")
            }

            DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false })
            {
                DropdownMenuItem(text = { Text("Editar") }, leadingIcon = { Icon(Lucide.Pencil, contentDescription = null) },
                    onClick = {
                        menuOpen = false
                        onEdit()
                    }
                )
                DropdownMenuItem(text = { Text("Arquivar") }, leadingIcon = { Icon(Lucide.Archive, contentDescription = null) },
                    onClick = {
                        menuOpen = false
                        // por fazer
                    }
                )
                DropdownMenuItem(text = { Text("Mover transações") }, leadingIcon = { Icon(Lucide.ArrowLeftRight, contentDescription = null) },
                    onClick = {
                        menuOpen = false
                        // por fazer
                    }
                )
            }
        }
    }
}