package com.example.engsftwgraph.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TitleText(text: String = "Login") {
    Text(text = text, style = MaterialTheme.typography.headlineMedium)
}