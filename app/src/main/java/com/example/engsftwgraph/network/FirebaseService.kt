package com.example.engsftwgraph.network

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.example.engsftwgraph.model.UserModel
import com.example.engsftwgraph.util.UserPreferences
import com.example.engsftwgraph.util.UserPreferences.saveUserData
import com.example.engsftwgraph.util.dateToTimestamp
import com.example.engsftwgraph.util.getFirebaseAuthErrorMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.random.Random
import com.github.mikephil.charting.data.Entry

object FirebaseService {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    @SuppressLint("StaticFieldLeak")
    private val db = Firebase.firestore

    fun createUserWithEmailAndPassword(
        context: Context,
        user: UserModel,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(user.email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = task.result?.user?.uid
                if (userId != null) {
                    val userMap = hashMapOf(
                        "name" to user.name,
                        "email" to user.email,
                        "acc_number" to user.accountNumber
                    )

                    db.collection("users").document(userId).set(userMap).addOnSuccessListener {
                        Log.d("FirebaseService", "User added successfully")
                        addMockDataToFirestore(
                            context = context,
                            onSuccess = {
                                Log.d(
                                    "FirebaseService",
                                    "Mock data added successfully after login"
                                )
                            },
                            onFailure = { e ->
                                val errorMessage = getFirebaseAuthErrorMessage(task.exception)
                                onFailure(errorMessage)
                                Log.w("FirebaseService", "Failed to add mock data", e)
                            }
                        )
                        onSuccess()
                    }.addOnFailureListener { e ->
                        Log.w("FirebaseService", "Error adding user", e)
                        val errorMessage = getFirebaseAuthErrorMessage(task.exception)
                        onFailure(errorMessage)
                    }
                } else {
                    val errorMessage = getFirebaseAuthErrorMessage(task.exception)
                    onFailure(errorMessage)
                }
            } else {
                val errorMessage = getFirebaseAuthErrorMessage(task.exception)
                onFailure(errorMessage)
            }
        }
    }

    fun signInWithEmailAndPassword(
        context: Context,
        email: String,
        password: String,
        onSuccess: (UserModel) -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = task.result?.user?.uid
                if (userId != null) {
                    db.collection("users").document(userId).get()
                        .addOnSuccessListener { document ->
                            val name = document.getString("name") ?: ""
                            val accountNumber = document.getString("acc_number") ?: ""
                            val userModel = UserModel(name, email, accountNumber)
                            saveUserData(context, userModel)

                            onSuccess(userModel)
                        }
                        .addOnFailureListener { e ->
                            val errorMessage = getFirebaseAuthErrorMessage(task.exception)
                            onFailure(errorMessage)
                        }
                } else {
                    val errorMessage = getFirebaseAuthErrorMessage(task.exception)
                    onFailure(errorMessage)
                }
            } else {
                val errorMessage = getFirebaseAuthErrorMessage(task.exception)
                onFailure(errorMessage)
            }
        }
    }

    private fun addMockDataToFirestore(context: Context, onSuccess: () -> Unit, onFailure: (Exception?) -> Unit) {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            onFailure(Exception("Usuário não autenticado"))
            return
        }

        val userData = UserPreferences.getUserData(context)
        val userName = userData?.name ?: "Usuário Desconhecido"
        val accNumber = userData?.accountNumber ?: "000000000"

        // Geração de dados mockados
        val pointEntries = (1..10).map { i ->
            hashMapOf(
                "id" to "entry$i",
                "userName" to userName,
                "acc_number" to accNumber,
                "pointsAdded" to Random.nextInt(50, 500),
                "date" to "2024-09-${Random.nextInt(10, 20)}T${Random.nextInt(8, 18)}:00:00Z",
                "entityId" to "entity_${Random.nextInt(400, 500)}"
            )
        }

        val productRedemptions = (1..5).map { i ->
            hashMapOf(
                "id" to "redemption$i",
                "userName" to userName,
                "acc_number" to accNumber,
                "productId" to "prod_${Random.nextInt(700, 900)}",
                "pointsSpent" to Random.nextInt(100, 500),
                "date" to "2024-09-${Random.nextInt(10, 20)}T${Random.nextInt(10, 19)}:00:00Z"
            )
        }

        val moneyRedemptions = (1..5).map { i ->
            hashMapOf(
                "id" to "moneyRedemption$i",
                "userName" to userName,
                "acc_number" to accNumber,
                "amountRedeemed" to Random.nextInt(50, 300),
                "pointsSpent" to Random.nextInt(200, 600),
                "date" to "2024-09-${Random.nextInt(10, 20)}T${Random.nextInt(8, 18)}:00:00Z"
            )
        }

        // Adiciona os dados no Firestore usando coleções e subcoleções
        val pointEntriesCollection = db.collection("point_entries").document(userId).collection("entries")
        val productRedemptionsCollection = db.collection("product_redemptions").document(userId).collection("redemptions")
        val moneyRedemptionsCollection = db.collection("money_redemptions").document(userId).collection("money_redemptions")

        // Adicionando entradas de pontos
        pointEntries.forEach { entry ->
            pointEntriesCollection.add(entry)
                .addOnSuccessListener { Log.d("FirebaseService", "Ponto adicionado com sucesso!") }
                .addOnFailureListener { e -> onFailure(e) }
        }

        // Adicionando trocas de produtos
        productRedemptions.forEach { redemption ->
            productRedemptionsCollection.add(redemption)
                .addOnSuccessListener { Log.d("FirebaseService", "Troca de produto registrada!") }
                .addOnFailureListener { e -> onFailure(e) }
        }

        // Adicionando trocas por dinheiro
        moneyRedemptions.forEach { redemption ->
            moneyRedemptionsCollection.add(redemption)
                .addOnSuccessListener { Log.d("FirebaseService", "Troca por dinheiro registrada!") }
                .addOnFailureListener { e -> onFailure(e) }
        }

        onSuccess()
    }

    fun getPointEntries(onSuccess: (List<HashMap<String, Any>>) -> Unit, onFailure: (Exception?) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            onFailure(Exception("Usuário não autenticado"))
            return
        }
        db.collection("point_entries").document(userId).collection("entries")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val entries = querySnapshot.documents.map { it.data as HashMap<String, Any> }
                onSuccess(entries)
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getProductRedemptions(onSuccess: (List<HashMap<String, Any>>) -> Unit, onFailure: (Exception?) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            onFailure(Exception("Usuário não autenticado"))
            return
        }
        db.collection("product_redemptions").document(userId).collection("redemptions")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val redemptions = querySnapshot.documents.map { it.data as HashMap<String, Any> }
                onSuccess(redemptions)
            }
            .addOnFailureListener { e -> onFailure(e) }
    }
    fun getMoneyRedemptions(onSuccess: (List<HashMap<String, Any>>) -> Unit, onFailure: (Exception?) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            onFailure(Exception("Usuário não autenticado"))
            return
        }
        db.collection("money_redemptions").document(userId).collection("redemptions")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val redemptions = querySnapshot.documents.map { it.data as HashMap<String, Any> }
                onSuccess(redemptions)
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun calculatePointsBalance(onSuccess: (Int) -> Unit, onFailure: (Exception?) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            onFailure(Exception("Usuário não autenticado"))
            return
        }

        getPointEntries({ entries ->
            val totalIn = entries.sumOf { (it["pointsAdded"] as Long).toInt() }

            getProductRedemptions({ redemptions ->
                val totalOutProducts = redemptions.sumOf { (it["pointsSpent"] as Long).toInt() }

                getMoneyRedemptions({ moneyRedemptions ->
                    val totalOutMoney = moneyRedemptions.sumOf { (it["pointsSpent"] as Long).toInt() }
                    val totalOut = totalOutProducts + totalOutMoney
                    val balance = totalIn - totalOut

                    onSuccess(balance)
                }, onFailure)
            }, onFailure)
        }, onFailure)
    }

    fun getAllData(
        onSuccess: (List<Entry>) -> Unit,
        onFailure: (Exception?) -> Unit
    ) {
        val userId = auth.currentUser?.uid ?: run {
            onFailure(Exception("Usuário não autenticado"))
            return
        }

        val entriesList = mutableListOf<Entry>()

        // Obter entradas de pontos
        getPointEntries({ entries ->
            entries.forEach { entry ->
                val date = entry["date"] as? String
                val pointsAdded = (entry["pointsAdded"] as? Long)?.toFloat()
                if (date != null && pointsAdded != null) {
                    entriesList.add(Entry(dateToTimestamp(date), pointsAdded))
                } else {
                    Log.e("FirebaseService", "Dados de entrada inválidos: $entry")
                }
            }

            // Obter redemptions de produtos
            getProductRedemptions({ redemptions ->
                redemptions.forEach { redemption ->
                    val date = redemption["date"] as? String
                    val pointsSpent = (redemption["pointsSpent"] as? Long)?.toFloat()?.times(-1)
                    if (date != null && pointsSpent != null) {
                        entriesList.add(Entry(dateToTimestamp(date), pointsSpent))
                    } else {
                        Log.e("FirebaseService", "Dados de redenção de produto inválidos: $redemption")
                    }
                }

                // Obter redemptions de dinheiro
                getMoneyRedemptions({ moneyRedemptions ->
                    moneyRedemptions.forEach { moneyRedemption ->
                        val date = moneyRedemption["date"] as? String
                        val moneySpent = (moneyRedemption["moneySpent"] as? Long)?.toFloat()?.times(-1)
                        if (date != null && moneySpent != null) {
                            entriesList.add(Entry(dateToTimestamp(date), moneySpent))
                        } else {
                            Log.e("FirebaseService", "Dados de redenção de dinheiro inválidos: $moneyRedemption")
                        }
                    }

                    onSuccess(entriesList)
                }, onFailure)
            }, onFailure)
        }, onFailure)
    }

}



    fun logoutUser(context: Context) {
    FirebaseAuth.getInstance().signOut()
    UserPreferences.clearUserData(context)
}
