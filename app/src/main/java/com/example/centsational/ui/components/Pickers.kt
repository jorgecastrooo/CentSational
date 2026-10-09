package com.example.centsational.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

private const val VISIBLE_COUNT = 3

@Composable
private fun ColorDot(colorHex: Long, selected: Boolean, onClick: () -> Unit)
{
    Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(colorHex))
            .border(
                width = if (selected) 3.dp else 0.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = CircleShape
            )
            .clickable(onClick = onClick)
    )
}

@Composable
private fun IconTile(vector: ImageVector, name: String, selected: Boolean, selectedColor: Long, onClick: () -> Unit)
{
    Box(modifier = Modifier.size(40.dp).clip(CircleShape)
            .background(
                if (selected) Color(selectedColor)
                else MaterialTheme.colorScheme.surfaceVariant
            )
            .clickable(onClick = onClick), contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = vector, contentDescription = name, tint = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PickerDialog(title: String, onDismiss: () -> Unit, content: @Composable () -> Unit)
{
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false))
    {
        Surface(modifier = Modifier.fillMaxWidth(0.92f).heightIn(max = 560.dp), shape = RoundedCornerShape(24.dp), tonalElevation = 6.dp)
        {
            Column(modifier = Modifier.padding(16.dp))
            {
                Text(text = title, style = MaterialTheme.typography.titleMedium)

                Column(modifier = Modifier.weight(1f, fill = false).verticalScroll(rememberScrollState()).padding(vertical = 16.dp))
                {
                    content()
                }

                TextButton(onClick = onDismiss, modifier = Modifier.align(Alignment.End))
                {
                    Text("Fechar")
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ColorPicker(colors: List<Long>, selected: Long, onSelect: (Long) -> Unit)
{
    var showAll by remember { mutableStateOf(false) }

    val base = colors.take(VISIBLE_COUNT)
    val visible = if (selected in base) base else base.dropLast(1) + selected

    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically)
    {
        visible.forEach { colorHex ->
            ColorDot(colorHex, colorHex == selected) { onSelect(colorHex) }
        }
        OutlinedButton(onClick = { showAll = true }) {
            Text("Outros")
        }
    }

    if (showAll)
    {
        PickerDialog(title = "Escolhe uma cor", onDismiss = { showAll = false })
        {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp))
            {
                colors.forEach { colorHex ->
                    ColorDot(colorHex, colorHex == selected) {
                        onSelect(colorHex)
                        showAll = false
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IconPicker(icons: Map<String, ImageVector>, selected: String, selectedColor: Long, onSelect: (String) -> Unit)
{
    var showAll by remember { mutableStateOf(false) }

    val base = icons.keys.toList().take(VISIBLE_COUNT)
    val visible = if (selected in base) base else base.dropLast(1) + selected

    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically)
    {
        visible.forEach { key ->
            icons[key]?.let { vector ->
                IconTile(vector, key, key == selected, selectedColor) { onSelect(key) }
            }
        }
        OutlinedButton(onClick = { showAll = true })
        {
            Text("Outros")
        }
    }

    if (showAll)
    {
        PickerDialog(title = "Escolhe um ícone", onDismiss = { showAll = false }) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp))
            {
                icons.forEach { (key, vector) ->
                    IconTile(vector, key, key == selected, selectedColor) {
                        onSelect(key)
                        showAll = false
                    }
                }
            }
        }
    }
}