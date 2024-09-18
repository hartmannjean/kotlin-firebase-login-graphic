package com.example.engsftwgraph.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
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
                            val errorMessage = getFirebaseAuthErrorMessage(task.exception)
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        )
    }
}

@Composable
fun TitleText() {
    Text(text = "Firebase Login", style = MaterialTheme.typography.bodyMedium)
}

@Composable
fun EmailField(email: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = email,
        onValueChange = onValueChange,
        label = { Text("Email") },
        modifier = Modifier.fillMaxWidth()
    )
}

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

@Composable
fun LoginButton(isLoading: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        enabled = !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(20.dp))
        } else {
            Text("Login")
        }
    }
}

fun getFirebaseAuthErrorMessage(exception: Exception?): String {
    return when (exception?.message) {
        "There is no user record corresponding to this identifier. The user may have been deleted." ->
            "Usuário não encontrado."
        "The password is invalid or the user does not have a password." ->
            "Senha incorreta."
        "The email address is badly formatted." ->
            "Formato de email inválido."
        "A network error (such as timeout, interrupted connection or unreachable host) has occurred." ->
            "Erro de rede. Verifique sua conexão."
        "The user account has been disabled by an administrator." ->
            "Conta desativada."
        "The password must be 6 characters long or more." ->
            "A senha deve ter pelo menos 6 caracteres."
        else -> "Erro desconhecido. Tente novamente."
    }
}