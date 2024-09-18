package com.example.engsftwgraph.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun PasswordField(password: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = password,
        onValueChange = onValueChange,
        label = { Text("Senha") },
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth()
    )
}