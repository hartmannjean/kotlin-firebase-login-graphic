package com.example.engsftwgraph.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.engsftwgraph.ui.components.EmailField
import com.example.engsftwgraph.ui.components.LoginButton
import com.example.engsftwgraph.ui.components.PasswordField
import com.example.engsftwgraph.ui.components.TitleText
import com.example.engsftwgraph.ui.components.CustomSnackbar
import com.example.engsftwgraph.util.getFirebaseAuthErrorMessage
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    Scaffold(
        snackbarHost = {
            if (showSnackbar) {
                CustomSnackbar(
                    message = snackbarMessage,
                    onDismiss = { showSnackbar = false }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TitleText()

            Spacer(modifier = Modifier.height(20.dp))

            EmailField(email) { email = it }

            Spacer(modifier = Modifier.height(16.dp))

            PasswordField(password) { password = it }

            Spacer(modifier = Modifier.height(20.dp))

            LoginButton(
                isLoading = isLoading,
                onClick = {
                    if (email.isEmpty() || password.isEmpty()) {
                        snackbarMessage = "Por favor, preencha todos os campos."
                        showSnackbar = true
                        return@LoginButton
                    }
                    isLoading = true
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                navController.navigate("home") {
                                    popUpTo("login") {
                                        inclusive = true
                                    }
                                }
                            } else {
                                snackbarMessage = getFirebaseAuthErrorMessage(task.exception)
                                showSnackbar = true
                            }
                        }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = {
                navController.navigate("signup")
            }) {
                Text(text = "Criar conta", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
