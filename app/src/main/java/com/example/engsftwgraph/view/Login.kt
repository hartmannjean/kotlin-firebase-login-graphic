package com.example.engsftwgraph.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.engsftwgraph.R
import com.example.engsftwgraph.navigation.AppRoutes
import com.example.engsftwgraph.ui.components.EmailField
import com.example.engsftwgraph.ui.components.LoginButton
import com.example.engsftwgraph.ui.components.PasswordField
import com.example.engsftwgraph.ui.components.TitleText
import com.example.engsftwgraph.ui.components.CustomSnackbar
import com.example.engsftwgraph.viewModel.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = viewModel()
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            if (showSnackbar) {
                CustomSnackbar(snackbarHostState, snackbarMessage) {
                    showSnackbar = false
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp, start = 16.dp, end = 16.dp)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(100.dp),
                colorFilter = ColorFilter.tint(Color(0xFF6650a4))
            )
            Spacer(modifier = Modifier.height(120.dp))
            TitleText()

            EmailField(email) { email = it }

            PasswordField(password = password, { password = it })

            LoginButton(
                isLoading = isLoading,
                onClick = {
                    if (email.isEmpty() || password.isEmpty()) {
                        snackbarMessage = "Por favor, preencha todos os campos."
                        showSnackbar = true
                        return@LoginButton
                    }
                    isLoading = true
                    viewModel.signIn(
                        context,
                        email = email,
                        password = password,
                        onSuccess = {
                            isLoading = false
                            navController.navigate(AppRoutes.HOME) {
                                popUpTo(AppRoutes.LOGIN) { inclusive = true }
                            }
                        },
                        onError = { error ->
                            isLoading = false
                            snackbarMessage = error
                            showSnackbar = true
                        }
                    )
                }
            )

            TextButton(onClick = {
                navController.navigate("signup")
            }) {
                Text(text = "Criar conta", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}