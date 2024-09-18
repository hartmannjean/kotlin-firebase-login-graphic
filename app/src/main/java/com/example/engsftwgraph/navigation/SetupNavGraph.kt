package com.example.engsftwgraph.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.example.engsftwgraph.view.HomeScreen
import com.example.engsftwgraph.view.LoginScreen
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import com.example.engsftwgraph.checkUserLoggedIn


@Composable
fun AppNavHost(navController: NavHostController) {
    LaunchedEffect(Unit) {
        checkUserLoggedIn(navController)
    }
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("home") {
            HomeScreen(navController = navController)
        }
    }
}
