package com.example.engsftwgraph.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.engsftwgraph.model.UserModel
import com.example.engsftwgraph.network.FirebaseService
import com.example.engsftwgraph.util.UserPreferences

class HomeViewModel : ViewModel() {

    fun getUserData(
        context: Context,
        onDataFetched: (UserModel?) -> Unit
    ) {
        val userData = UserPreferences.getUserData(context)
        onDataFetched(userData)
    }

    fun calculatePointsBalance(
        onBalanceCalculated: (Int) -> Unit,
        onError: (String) -> Unit
    ) {
        FirebaseService.calculatePointsBalance({ balance ->
            onBalanceCalculated(balance)
        }, { exception ->
            onError(exception?.message ?: "Erro ao calcular saldo de pontos.")
        })
    }
}

