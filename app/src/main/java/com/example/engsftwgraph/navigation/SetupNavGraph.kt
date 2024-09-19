package com.example.engsftwgraph.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.engsftwgraph.view.HomeScreen
import com.example.engsftwgraph.view.LoginScreen
import com.example.engsftwgraph.view.SignUpScreen
import com.example.engsftwgraph.viewModel.LoginViewModel
import com.example.engsftwgraph.viewModel.SignUpViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavHost(navController: NavHostController) {
    LaunchedEffect(Unit) {
        checkUserLoggedIn(navController)
    }
    NavHost(navController = navController, startDestination = AppRoutes.LOGIN) {
        composable(AppRoutes.LOGIN) {
            val loginViewModel: LoginViewModel = viewModel()
            LoginScreen(navController = navController, viewModel = loginViewModel)
        }
        composable(AppRoutes.HOME) {
            HomeScreen(navController = navController)
        }
        composable(AppRoutes.SIGNUP) {
            val signUpViewModel: SignUpViewModel = viewModel()
            SignUpScreen(navController = navController, viewModel = signUpViewModel)
        }
    }
}

fun checkUserLoggedIn(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    if (currentUser != null) {
        navController.navigate(AppRoutes.HOME) {
            popUpTo(AppRoutes.LOGIN) {
                inclusive = true
            }
        }
    } else {
        navController.navigate(AppRoutes.LOGIN) {
            popUpTo(AppRoutes.HOME) {
                inclusive = true
            }
        }
    }
}