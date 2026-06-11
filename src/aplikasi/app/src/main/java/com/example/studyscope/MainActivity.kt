package com.example.studyscope

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studyscope.screen.BerandaScreen
import com.example.studyscope.screen.LoginScreen
import com.example.studyscope.screen.MataKuliahScreen
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
    var authToken by remember { mutableStateOf<String?>(null) }

    NavHost(navController = navController, startDestination = "login") {

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

        composable("beranda") {
            if (authToken != null) {
                BerandaScreen(
                    token = authToken!!,
                    onNavigateToMatkul = {
                        navController.navigate("matakuliah")
                    }
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate("login") { popUpTo(0) }
                }
            }
        }

        // Layar 4: Mata Kuliah
        composable("matakuliah") {
            if (authToken != null) {
                MataKuliahScreen(
                    token = authToken!!,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToDetail = { idMatkul ->
                        // TODO: nanti ke halaman detail
                        // navController.navigate("detail_matkul/$idMatkul")
                    }
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate("login") { popUpTo(0) }
                }
            }
        }
    }
}