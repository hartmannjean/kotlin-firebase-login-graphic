package com.example.engsftwgraph.network

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.example.engsftwgraph.model.UserModel
import com.example.engsftwgraph.util.UserPreferences
import com.example.engsftwgraph.util.UserPreferences.saveUserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object FirebaseService {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    @SuppressLint("StaticFieldLeak")
    private val db = Firebase.firestore

    fun createUserWithEmailAndPassword(
        user: UserModel,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Exception?) -> Unit
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
                        onSuccess()
                    }.addOnFailureListener { e ->
                        Log.w("FirebaseService", "Error adding user", e)
                        onFailure(e)
                    }
                } else {
                    onFailure(Exception("UID não encontrado"))
                }
            } else {
                onFailure(task.exception)
            }
        }
    }

    fun signInWithEmailAndPassword(
        context: android.content.Context,
        email: String,
        password: String,
        onSuccess: (UserModel) -> Unit,
        onFailure: (Exception?) -> Unit
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
                            onFailure(e)
                        }
                } else {
                    onFailure(Exception("Usuário não encontrado"))
                }
            } else {
                onFailure(task.exception)
            }
        }
    }
}
fun logoutUser(context: Context) {
    FirebaseAuth.getInstance().signOut()
    UserPreferences.clearUserData(context)
}
