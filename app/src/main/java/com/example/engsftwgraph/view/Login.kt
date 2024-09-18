package com.example.engsftwgraph.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.engsftwgraph.ui.components.EmailField
import com.example.engsftwgraph.ui.components.LoginButton
import com.example.engsftwgraph.ui.components.PasswordField
import com.example.engsftwgraph.ui.components.TitleText
import com.example.engsftwgraph.util.getFirebaseAuthErrorMessage
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

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = {
            navController.navigate("signup")
        }) {
            Text(text = "Criar conta", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

