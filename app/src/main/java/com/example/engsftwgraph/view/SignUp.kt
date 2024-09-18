package com.example.engsftwgraph.view

import CreateAccountButton
import androidx.compose.runtime.Composable
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.engsftwgraph.ui.components.CustomSnackbar
import com.example.engsftwgraph.ui.components.EmailField
import com.example.engsftwgraph.ui.components.PasswordField
import com.example.engsftwgraph.util.getFirebaseAuthErrorMessage
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SignUpScreen(navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
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
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Criar Conta", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        EmailField(email) { email = it }

        Spacer(modifier = Modifier.height(16.dp))

        PasswordField(password) { password = it }

        Spacer(modifier = Modifier.height(16.dp))

        PasswordField(confirmPassword) { confirmPassword = it }

        Spacer(modifier = Modifier.height(20.dp))

        CreateAccountButton(
            isLoading = isLoading,
            onClick = {
                if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    snackbarMessage = "Por favor, preencha todos os campos."
                    showSnackbar = true
                    return@CreateAccountButton
                }
                if (password == confirmPassword) {
                    isLoading = true
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                navController.navigate("home") {
                                    popUpTo("signup") {
                                        inclusive = true
                                    }
                                }
                            } else {
                                snackbarMessage = getFirebaseAuthErrorMessage(task.exception)
                                showSnackbar = true
                            }
                        }
                } else {
                    Toast.makeText(context, "As senhas não coincidem", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
    }
}

