package com.example.studyscope

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studyscope.screen.ArsipScreen
import com.example.studyscope.screen.BerandaScreen
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
    val authViewModel: AuthViewModel = viewModel()

    var authToken    by remember { mutableStateOf<String?>(null) }
    var authUsername by remember { mutableStateOf("") }

    val authUsernameFromVm by authViewModel.username.collectAsState()
    LaunchedEffect(authUsernameFromVm) {
        if (authUsernameFromVm.isNotBlank()) authUsername = authUsernameFromVm
    }

    NavHost(navController = navController, startDestination = "login") {

        // Layar 1: Login
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = { navController.navigate("register") },
                onLoginSuccess = { token ->
                    authToken = token
                    navController.navigate("beranda") {
                        popUpTo("login") { inclusive = true }
                    }
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
                    navController.navigate("beranda") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }

        // Layar 3: Beranda
        composable("beranda") {
            if (authToken != null) {
                BerandaScreen(
                    token = authToken!!,
                    onNavigateToMatkul = {
                        // Nanti diarahkan ke detail matkul
                    },
                    onNavigateToArsip = {
                        navController.navigate("arsip")
                    },
                    onLogout = {
                        authToken = null
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                }
            }
        }

        // Layar 4: Arsip
        composable("arsip") {
            ArsipScreen(
                token = authToken ?: "",
                username = authUsername,
                onNavigateToBeranda = {
                    navController.navigate("beranda") {
                        popUpTo("beranda") { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onOpenDocument = { _, _ -> }, // dikerjakan temanmu nanti
                onLogout = {
                    authToken = null
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}