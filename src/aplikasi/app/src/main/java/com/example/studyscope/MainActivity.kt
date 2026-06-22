package com.example.studyscope

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.studyscope.screen.ArsipScreen
import com.example.studyscope.screen.BerandaScreen
import com.example.studyscope.screen.DetailMatkulScreen
import com.example.studyscope.screen.LoginScreen
import com.example.studyscope.screen.MataKuliahScreen
import com.example.studyscope.screen.PdfViewerScreen
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
                    onNavigateToMatkul = { query ->
                        if (query.isNotBlank()) {
                            navController.navigate("matakuliah?query=$query")
                        } else {
                            navController.navigate("matakuliah")
                        }
                    },
                    onNavigateToDetail = { idMatkul ->
                        navController.navigate("detail_matkul/$idMatkul")
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
                LaunchedEffect(Unit) { navController.navigate("login") { popUpTo(0) } }
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
                onOpenDokumen = { kodeRahasia ->
                    navController.navigate("pdf_viewer/$kodeRahasia")
                },
                onNavigateToLibrary = {
                    navController.navigate("matakuliah")
                },
                onLogout = {
                    authToken = null
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Layar 5: Mata Kuliah
        composable(
            route = "matakuliah?query={query}",
            arguments = listOf(navArgument("query") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""

            if (authToken != null) {
                MataKuliahScreen(
                    token = authToken!!,
                    initialQuery = query,
                    onNavigateToBeranda = {
                        navController.navigate("beranda") {
                            popUpTo("beranda") { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                    onNavigateToDetail = { idMatkul ->
                        navController.navigate("detail_matkul/$idMatkul")
                    },
                    onNavigateToArsip = {
                        navController.navigate("arsip")
                    },
                    onLogout = {
                        authToken = null
                        navController.navigate("login") { popUpTo(0) { inclusive = true } }
                    }
                )
            } else {
                LaunchedEffect(Unit) { navController.navigate("login") { popUpTo(0) } }
            }
        }

        // Layar 6: Detail Mata Kuliah
        composable("detail_matkul/{idMatkul}") { backStackEntry ->
            val idMatkul = backStackEntry.arguments?.getString("idMatkul")?.toIntOrNull() ?: 0

            if (authToken != null) {
                DetailMatkulScreen(
                    token = authToken!!,
                    idMatkul = idMatkul,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToBeranda = {
                        navController.navigate("beranda") {
                            popUpTo("beranda") { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                    onNavigateToArsip = {
                        navController.navigate("arsip")
                    },
                    onOpenDokumen = { kodeRahasia ->
                        navController.navigate("pdf_viewer/$kodeRahasia")
                    },
                    onLogout = {
                        authToken = null
                        navController.navigate("login") { popUpTo(0) { inclusive = true } }
                    }
                )
            } else {
                LaunchedEffect(Unit) { navController.navigate("login") { popUpTo(0) } }
            }
        }

        // Layar 7: Pembaca PDF Khusus
        composable("pdf_viewer/{kodeRahasia}") { backStackEntry ->
            val kodeRahasia = backStackEntry.arguments?.getString("kodeRahasia") ?: ""
            if (authToken != null) {
                PdfViewerScreen(
                    kodeRahasia = kodeRahasia,
                    token = authToken!!,
                    onNavigateBack = { navController.popBackStack() }
                )
            } else {
                LaunchedEffect(Unit) { navController.navigate("login") { popUpTo(0) } }
            }
        }
    }
}