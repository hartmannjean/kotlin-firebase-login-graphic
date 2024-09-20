package com.example.engsftwgraph.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomSnackbar(
    snackbarHostState: SnackbarHostState,
    message: String,
    onDismiss: () -> Unit
) {
    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            onDismiss()
        }
    }

    SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier.padding(16.dp),
        snackbar = {
            Snackbar(
                containerColor = Color(0xFFFFA500),
                contentColor = Color.White,
                action = {
                    TextButton(onClick = onDismiss) {
                        Text("Fechar")
                    }
                }
            ) {
                Row {
                    Icon(imageVector = Icons.Outlined.Clear, contentDescription = "Error")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = message)
                }
            }
        }
    )
}