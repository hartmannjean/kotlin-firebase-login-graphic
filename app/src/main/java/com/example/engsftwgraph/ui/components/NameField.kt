package com.example.engsftwgraph.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun NameField(name: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = name,
        onValueChange = onValueChange,
        label = { Text("Nome") },
        modifier = Modifier.fillMaxWidth()
    )
}