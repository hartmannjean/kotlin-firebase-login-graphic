package com.example.engsftwgraph.viewModel

import androidx.lifecycle.ViewModel
import com.example.engsftwgraph.model.UserModel
import com.example.engsftwgraph.network.FirebaseService

class SignUpViewModel : ViewModel() {

    fun createUser(
        name: String,
        email: String,
        accountNumber: String,
        password: String,
        confirmPassword: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (password != confirmPassword) {
            onError("As senhas não coincidem")
            return
        }

        val user = UserModel(
            name = name,
            email = email,
            accountNumber = accountNumber
        )

        FirebaseService.createUserWithEmailAndPassword(
            user = user,
            password = password,
            onSuccess = onSuccess,
            onFailure = { exception ->
                onError(exception?.message ?: "Erro desconhecido")
            }
        )
    }
}
