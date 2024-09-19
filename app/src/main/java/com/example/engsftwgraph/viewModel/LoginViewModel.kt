package com.example.engsftwgraph.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.engsftwgraph.network.FirebaseService
import com.example.engsftwgraph.util.UserPreferences

class LoginViewModel : ViewModel() {

    fun signIn(
        context: Context,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (email.isEmpty() || password.isEmpty()) {
            onError("Email ou senha não podem estar vazios.")
            return
        }

        FirebaseService.signInWithEmailAndPassword(
            context = context,
            email = email,
            password = password,
            onSuccess = { userModel ->
                UserPreferences.saveUserData(context, userModel)
                onSuccess()
            },
            onFailure = { exception ->
                onError(exception?.message ?: "Erro desconhecido")
            }
        )
    }
}