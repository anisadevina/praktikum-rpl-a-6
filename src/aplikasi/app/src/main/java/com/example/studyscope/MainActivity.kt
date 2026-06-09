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
import com.example.studyscope.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudyScopeApp()
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
                    authToken = token // 1. Simpan tokennya

                    // 2. Pindah ke Beranda, dan hancurkan "login" dari riwayat tombol Back HP
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
                    authToken = token // 1. Simpan tokennya

                    // 2. Pindah ke Beranda, dan hancurkan "register" dari riwayat tombol Back HP
                    navController.navigate("beranda") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }
        // --- LAYAR 3: BERANDA ---
        composable("beranda") {
            // Kita pastikan token tidak kosong sebelum membuka Beranda
            if (authToken != null) {
                BerandaScreen(
                    token = authToken!!, // Kirim token ke BerandaScreen
                    onNavigateToMatkul = {
                        // TODO: Nanti arahkan ke halaman detail mata kuliah
                        // navController.navigate("matkul_detail")
                    }
                )
            } else {
                // Keamanan ekstra: Jika token tiba-tiba hilang/null, tendang balik ke layar Login
                LaunchedEffect(Unit) {
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                }
            }
        }
    }
}