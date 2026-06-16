package com.example.studyscope.screen

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
import androidx.compose.ui.tooling.preview.Preview
import com.example.studyscope.ui.theme.StudyScopeTheme

// --- PENGATURAN WARNA DARI DESAINMU ---
val BackgroundColor = Color(0xFFF7F4E9) // Krem terang
val DarkGreen = Color(0xFF386641)       // Hijau tua (Header & Bottom Nav)
val CardGreen = Color(0xFF6A994E)       // Hijau sedang (Kartu Matkul)
val LightGreen = Color(0xFFA7C957)      // Hijau muda (Tombol)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BerandaScreen(
    token: String,
    onNavigateToMatkul: () -> Unit,
    viewModel: BerandaViewModel = viewModel()
) {
    val berandaData by viewModel.berandaData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    BerandaContent(
        username = berandaData?.user?.username ?: "Pengguna",
        searchQuery = searchQuery,
        onSearchQueryChange = { viewModel.updateSearchQuery(it) },
        isLoading = isLoading,
        error = error,
        matkulList = berandaData?.mataKuliahTerakhir ?: emptyList(),
        onNavigateToMatkul = onNavigateToMatkul
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
    onNavigateToMatkul: () -> Unit
) {
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = DarkGreen,
                contentColor = Color.White,
                modifier = Modifier.clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Beranda") },
                    selected = true,
                    onClick = { },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DarkGreen,
                        selectedTextColor = Color.White,
                        indicatorColor = Color.White,
                        unselectedIconColor = Color.LightGray
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = "Arsip") },
                    selected = false,
                    onClick = { },
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
        containerColor = BackgroundColor
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(DarkGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.Transparent,
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkGreen)
                    ) {
                        Text(
                            text = username,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = DarkGreen,
                            fontSize = 12.sp
                        )
                    }
                }

                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .background(Color(0xFFBC4749), CircleShape)
                        .size(40.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Cari mata kuliah", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DarkGreen,
                    unfocusedBorderColor = Color.Transparent,
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkGreen)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "Halo, $username!", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Selamat datang di Study Scope", color = Color.White, fontSize = 14.sp)
                    Text(text = "Ayo mulai perjalananmu dengan mengakses menu mata kuliah dan arsip", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Akses Terakhir Mata Kuliah", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            if (isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = DarkGreen)
                }
            } else if (error != null) {
                Text(text = error, color = MaterialTheme.colorScheme.error)
            } else {
                if (matkulList.isEmpty()) {
                    Text("Belum ada data mata kuliah.", color = Color.Gray)
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
                                colors = CardDefaults.cardColors(containerColor = CardGreen),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(100.dp)
                                            .background(Color.White),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Image, contentDescription = null, tint = Color.LightGray)
                                    }

                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(text = matkul.nama_matkul, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("Tingkat kesulitan", fontSize = 10.sp, color = Color.Black)
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFFFFC107), modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(text = "${matkul.tingkat_kesulitan}/5", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("${matkul.arsip} arsip (materi, tugas, soal)", fontSize = 9.sp, color = Color.Black.copy(alpha = 0.7f))

                                        Spacer(modifier = Modifier.height(8.dp))
                                        Button(
                                            onClick = onNavigateToMatkul,
                                            colors = ButtonDefaults.buttonColors(containerColor = LightGreen),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.fillMaxWidth(),
                                            contentPadding = PaddingValues(0.dp)
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
    StudyScopeTheme {
        BerandaContent(
            username = "Andi Saja",
            searchQuery = "",
            onSearchQueryChange = {},
            isLoading = false,
            error = null,
            matkulList = listOf(
                com.example.studyscope.model.Matkul(1, "Algoritma", 4.5, 10),
                com.example.studyscope.model.Matkul(2, "Struktur Data", 4.0, 8)
            ),
            onNavigateToMatkul = {}
        )
    }
}
