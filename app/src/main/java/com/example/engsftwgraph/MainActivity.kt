package com.example.engsftwgraph

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.engsftwgraph.navigation.AppNavHost
import com.example.engsftwgraph.ui.theme.EngsftwGraphTheme
import com.google.firebase.auth.FirebaseAuth


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AppNavHost(navController = navController)
        }
    }
}

fun checkUserLoggedIn(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    if (currentUser != null) {
        navController.navigate("home") {
            popUpTo("login") {
                inclusive = true
            }
        }
    } else {
        navController.navigate("login") {
            popUpTo("home") {
                inclusive = true
            }
        }
    }
}
