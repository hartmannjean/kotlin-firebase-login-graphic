package com.example.engsftwgraph.view

import CreateAccountButton
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.engsftwgraph.ui.components.AccountField
import com.example.engsftwgraph.ui.components.CustomSnackbar
import com.example.engsftwgraph.ui.components.EmailField
import com.example.engsftwgraph.ui.components.NameField
import com.example.engsftwgraph.ui.components.PasswordField
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.engsftwgraph.viewModel.SignUpViewModel

@Composable
fun SignUpScreen(
    navController: NavController, viewModel: SignUpViewModel = viewModel()
) {

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var accountnumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    Scaffold(snackbarHost = {
        if (showSnackbar) {
            CustomSnackbar(message = snackbarMessage, onDismiss = { showSnackbar = false })
        }
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically)
        ) {
            Text(text = "Criar Conta", style = MaterialTheme.typography.headlineMedium)

            NameField(name) { name = it }

            AccountField(accountnumber) { accountnumber = it }

            EmailField(email) { email = it }

            PasswordField(password) { password = it }

            PasswordField(confirmPassword) { confirmPassword = it }

            CreateAccountButton(isLoading = isLoading, onClick = {
                if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    snackbarMessage = "Por favor, preencha todos os campos."
                    showSnackbar = true
                    return@CreateAccountButton
                }

                isLoading = true
                viewModel.createUser(name = name,
                    email = email,
                    accountNumber = accountnumber,
                    password = password,
                    confirmPassword = confirmPassword,
                    onSuccess = {
                        isLoading = false
                        navController.navigate("home") {
                            popUpTo("signup") {
                                inclusive = true
                            }
                        }
                    },
                    onError = { errorMessage ->
                        isLoading = false
                        snackbarMessage = errorMessage
                        showSnackbar = true
                    })
            })
        }
    }
}

