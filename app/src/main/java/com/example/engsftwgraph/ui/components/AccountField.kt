package com.example.engsftwgraph.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AccountField(number: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = number,
        onValueChange = { newValue ->
            if (newValue.length <= 9 && newValue.all { it.isDigit() }) {
                onValueChange(newValue)
            }
        },
        label = { Text("Número da conta") },
        modifier = Modifier.fillMaxWidth()
    )
}