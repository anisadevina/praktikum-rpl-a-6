package com.example.studyscope

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studyscope.screen.LoginScreen
import com.example.studyscope.screen.RegisterScreen
import com.example.studyscope.ui.theme.StudyScopeTheme
import com.example.studyscope.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudyScopeTheme {
                StudyScopeApp()
            }
        }
    }
}

@Composable
fun StudyScopeApp() {
    val navController = rememberNavController()
    // Menggunakan 1 ViewModel yang sama untuk Login dan Register
    val authViewModel: AuthViewModel = viewModel()

    // Menyimpan token login sementara
    var authToken by remember { mutableStateOf<String?>(null) }

    NavHost(navController = navController, startDestination = "login") {

        // Layar 1: Login
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = { navController.navigate("register") },
                onLoginSuccess = { token ->
                    authToken = token
                    // TODO: Arahkan ke halaman Beranda nanti
                    println("Login Sukses! Token: $token")
                }
            )
        }

        // Layar 2: Register
        composable("register") {
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateToLogin = { navController.navigate("login") },
                onRegisterSuccess = { token ->
                    authToken = token
                    // TODO: Arahkan ke halaman Beranda nanti
                    println("Register Sukses! Token: $token")
                }
            )
        }
    }
}