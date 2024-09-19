package com.example.engsftwgraph.util

import android.content.Context
import android.content.SharedPreferences
import com.example.engsftwgraph.model.UserModel

object UserPreferences {

    private const val PREF_NAME = "user_prefs"
    private const val KEY_NAME = "name"
    private const val KEY_EMAIL = "email"
    private const val KEY_ACCOUNT_NUMBER = "account_number"

    fun saveUserData(context: Context, user: UserModel) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString(KEY_NAME, user.name)
        editor.putString(KEY_EMAIL, user.email)
        editor.putString(KEY_ACCOUNT_NUMBER, user.accountNumber)
        editor.apply()
    }

    fun getUserData(context: Context): UserModel? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        val name = sharedPreferences.getString(KEY_NAME, null)
        val email = sharedPreferences.getString(KEY_EMAIL, null)
        val accountNumber = sharedPreferences.getString(KEY_ACCOUNT_NUMBER, null)

        return if (name != null && email != null && accountNumber != null) {
            UserModel(name, email, accountNumber)
        } else {
            null
        }
    }

    fun clearUserData(context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }
}
