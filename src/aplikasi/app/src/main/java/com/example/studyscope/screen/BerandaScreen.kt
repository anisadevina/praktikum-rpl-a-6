package com.example.studyscope.screen

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studyscope.ui.theme.HunterGreen
import com.example.studyscope.viewmodel.BerandaViewModel
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BerandaScreen(
    token: String,
    onNavigateToMatkul: (String) -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToArsip: () -> Unit,
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
        onNavigateToDetail = onNavigateToDetail,
        onNavigateToArsip = onNavigateToArsip,
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
    onNavigateToMatkul: (String) -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToArsip: () -> Unit,
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
                    icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = "Mata Kuliah") },
                    selected = false,
                    onClick = { onNavigateToMatkul("") },
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.LightGray)
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.BookmarkBorder, contentDescription = "Arsip") },
                    selected = false,
                    onClick = onNavigateToArsip,
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
                .padding(horizontal = 24.dp)
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
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, HunterGreen)
                    ) {
                        Text(
                            text = username,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = HunterGreen,
                            fontSize = 12.sp
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
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(24.dp))
                    .heightIn(min = 40.dp),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                ),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(fontSize = 13.sp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        if (searchQuery.isNotBlank()) {
                            onNavigateToMatkul(searchQuery)
                            onSearchQueryChange("")
                        }
                    }
                )
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
                                            onClick = { onNavigateToDetail(matkul.id_matkul) },
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BerandaScreenPreview() {
    com.example.studyscope.ui.theme.StudyScopeTheme {
        BerandaContent(
            username = "nama pengguna",
            searchQuery = "",
            onSearchQueryChange = {},
            isLoading = false,
            error = null,
            matkulList = listOf(
                com.example.studyscope.model.Matkul(1, "Algoritma & Pemrograman", 4.5, 10),
                com.example.studyscope.model.Matkul(2, "Struktur Data", 4.0, 8),
                com.example.studyscope.model.Matkul(3, "Basis Data", 3.5, 12),
                com.example.studyscope.model.Matkul(4, "Jaringan Komputer", 4.2, 6),
            ),
            onNavigateToMatkul = {},
            onNavigateToDetail = {},
            onNavigateToArsip = {},
            onLogoutClick = {}
        )
    }
}