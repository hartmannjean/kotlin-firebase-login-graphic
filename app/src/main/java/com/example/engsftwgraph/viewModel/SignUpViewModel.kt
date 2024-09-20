package com.example.engsftwgraph.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.engsftwgraph.model.UserModel
import com.example.engsftwgraph.network.FirebaseService
import com.example.engsftwgraph.util.isValidEmail

class SignUpViewModel : ViewModel() {

    fun createUser(
        context: Context,
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
            context = context,
            user = user,
            password = password,
            onSuccess = onSuccess,
            onFailure = { exception ->
                onError(exception)
            }
        )
    }

    fun validateFields(
        name: String,
        email: String,
        accountNumber: String,
        password: String,
        confirmPassword: String
    ): String? {
        if (name.length < 4) {
            return "O nome deve ter pelo menos 4 caracteres."
        }
        if (email.isEmpty() || accountNumber.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            return "Por favor, preencha todos os campos."
        }
        if (!isValidEmail(email)) {
            return "Por favor, insira um email válido."
        }
        if (accountNumber.length != 9) {
            return "O número da conta deve ter exatamente 9 dígitos."
        }
        if (password != confirmPassword) {
            return "As senhas não coincidem."
        }
        return null
    }
}

