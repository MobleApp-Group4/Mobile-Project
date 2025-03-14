package com.example.todo.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FilterChip(category: String, selected: Boolean,onClick: () -> Unit) {
    ElevatedFilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(category) },
        modifier = Modifier.padding(end = 8.dp)
    )
}