package com.example.engsftwgraph.util

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

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
        "The email address is already in use by another account." ->
            "O email já está em uso."
        "Password should be at least 6 characters" ->
            "A senha deve ter pelo menos 6 caracteres."
        "The email is badly formatted" ->
            "Formato de email inválido."
        "A network error (timeout, interrupted connection, or unreachable host) occurred." ->
            "Erro de rede. Verifique sua conexão."
        else -> "Erro desconhecido. Tente novamente."
    }
}

fun formatAccountNumber(accountNumber: String): String {
    return if (accountNumber.length > 2) {
        "${accountNumber.dropLast(2)}-${accountNumber.takeLast(2)}"
    } else {
        accountNumber
    }
}

// Função auxiliar para converter a data em timestamp
fun dateToTimestamp(dateString: String): Float {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    return try {
        val date = formatter.parse(dateString)
        val timestamp = date?.time?.toFloat() ?: 0f
        Log.d("DateToTimestamp", "Converted $dateString to $timestamp")
        timestamp
    } catch (e: ParseException) {
        Log.e("DateError", "Invalid date format: $dateString", e)
        0f
    }
}

fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    return emailRegex.matches(email)
}

