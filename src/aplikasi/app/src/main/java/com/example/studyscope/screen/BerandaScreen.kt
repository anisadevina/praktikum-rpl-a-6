package com.example.studyscope.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studyscope.viewmodel.BerandaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BerandaScreen(
    token: String,
    onNavigateToMatkul: () -> Unit,
    onLogout: () -> Unit,
    viewModel: BerandaViewModel = viewModel()
) {
    val berandaData by viewModel.berandaData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val filteredMatkul by viewModel.filteredMatkul.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchBeranda(token)
    }

    BerandaContent(
        username = berandaData?.user?.username ?: "Memuat...",
        searchQuery = searchQuery,
        onSearchQueryChange = { viewModel.updateSearchQuery(it) },
        isLoading = isLoading,
        error = error,
        matkulList = filteredMatkul,
        onNavigateToMatkul = onNavigateToMatkul,
        onLogoutClick = {
            viewModel.logout(token) { onLogout() }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BerandaContent(
    username: String,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    isLoading: Boolean,
    error: String?,
    matkulList: List<com.example.studyscope.model.Matkul>,
    onNavigateToMatkul: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Beranda") },
                    selected = true,
                    onClick = { },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                        indicatorColor = MaterialTheme.colorScheme.onPrimary,
                        unselectedIconColor = Color.LightGray
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = "Arsip") },
                    selected = false,
                    onClick = onNavigateToMatkul,
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.LightGray)
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.BookmarkBorder, contentDescription = "Bookmark") },
                    selected = false,
                    onClick = { },
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.LightGray)
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // --- HEADER ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = "Profile", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.Transparent,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = username,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                IconButton(
                    onClick = onLogoutClick,
                    modifier = Modifier.background(MaterialTheme.colorScheme.error, CircleShape).size(40.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout", tint = MaterialTheme.colorScheme.onError)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- SEARCH BAR ---
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Cari mata kuliah", style = MaterialTheme.typography.bodyMedium, color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray) },
                modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Transparent,
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- HERO CARD ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "Halo, $username!", color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.headlineLarge)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Selamat datang di Study Scope", color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = "Ayo mulai perjalananmu dengan mengakses menu mata kuliah dan arsip",
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Akses Terakhir Mata Kuliah", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(12.dp))

            // --- GRID KARTU MATA KULIAH ---
            if (isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else if (error != null) {
                Text(text = error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
            } else {
                if (matkulList.isEmpty()) {
                    Text("Mata kuliah tidak ditemukan.", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(matkulList) { matkul ->
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = com.example.studyscope.ui.theme.SageGreen),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.height(260.dp)) { // Tinggi disamakan
                                    Box(
                                        modifier = Modifier.fillMaxWidth().height(100.dp).background(Color.White),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Image, contentDescription = null, tint = Color.LightGray)
                                    }

                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(text = matkul.nama_matkul, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text("Tingkat kesulitan", fontSize = 10.sp, color = Color.Black)
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Star, contentDescription = "Rating", tint = com.example.studyscope.ui.theme.StarYellow, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(2.dp))
                                            Text(text = "${matkul.tingkat_kesulitan}/5", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                        }
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text("${matkul.arsip} arsip (materi, tugas, soal)", fontSize = 9.sp, color = Color.Black.copy(alpha = 0.7f))
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Button(
                                            onClick = { /* Nanti arahkan ke detail */ },
                                            colors = ButtonDefaults.buttonColors(containerColor = com.example.studyscope.ui.theme.YellowGreen),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.fillMaxWidth().height(24.dp),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                                        ) {
                                            Text("Lihat Selengkapnya", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}