package com.example.studyscope

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studyscope.screen.ArsipScreen
import com.example.studyscope.screen.BerandaScreen
import com.example.studyscope.screen.DetailMatkulScreen
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
    var authUsername by remember { mutableStateOf<String?>(null) }

    NavHost(navController = navController, startDestination = "login") {

        // Layar 1: Login
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = { navController.navigate("register") },
                onLoginSuccess = { token, username ->
                    authToken = token
                    authUsername = username
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
                onRegisterSuccess = { token, username ->
                    authToken = token
                    authUsername = username
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
                    username = authUsername ?: "",
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToDetail = { idMatkul ->
                        navController.navigate("detail_matkul/$idMatkul")
                    },
                    onLogout = {
                        authToken = null
                        authUsername = null
                        navController.navigate("login") { popUpTo(0) }
                    }
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate("login") { popUpTo(0) }
                }
            }
        }

        // Layar 5: Detail Mata Kuliah
        composable("detail_matkul/{idMatkul}") { backStackEntry ->
            val idMatkul = backStackEntry.arguments?.getString("idMatkul")?.toIntOrNull() ?: 0
            if (authToken != null) {
                DetailMatkulScreen(
                    token = authToken!!,
                    username = authUsername ?: "",
                    idMatkul = idMatkul,
                    onNavigateBack = { navController.popBackStack() },
                    onLogout = {
                        authToken = null
                        authUsername = null
                        navController.navigate("login") { popUpTo(0) }
                    }
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate("login") { popUpTo(0) }
                }
            }
        }

        // Layar 6: Arsip
        composable("arsip") {
            ArsipScreen(
                onNavigateToBeranda = { navController.navigate("beranda") },
                onLogout = {
                    authToken = null
                    authUsername = null
                    navController.navigate("login") { popUpTo(0) }
                }
            )
        }
    }
}