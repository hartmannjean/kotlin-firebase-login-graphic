package com.example.engsftwgraph.util

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
