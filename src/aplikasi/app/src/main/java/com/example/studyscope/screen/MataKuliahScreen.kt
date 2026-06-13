package com.example.studyscope.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studyscope.model.Matkul
import com.example.studyscope.ui.theme.*
import com.example.studyscope.viewmodel.MataKuliahViewModel

@Composable
fun MataKuliahScreen(
    token: String,
    // ✅ Parameter username: String dihapus dari sini agar tidak error di MainActivity
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    onLogout: () -> Unit = {},
    viewModel: MataKuliahViewModel = viewModel()
) {
    val matkulList by viewModel.matkulList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val currentUsername by viewModel.username.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchMatkul(token = token)
    }

    MataKuliahContent(
        username = currentUsername,
        searchQuery = searchQuery,
        onSearchQueryChange = { query -> viewModel.updateSearchQuery(query, token) },
        isLoading = isLoading,
        error = error,
        matkulList = matkulList,
        onNavigateBack = onNavigateBack,
        onNavigateToDetail = onNavigateToDetail,
        onLogout = onLogout
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MataKuliahContent(
    username: String, // tambah ini
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    isLoading: Boolean,
    error: String?,
    matkulList: List<Matkul>,
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    onLogout: () -> Unit = {}
){
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = HunterGreen,
                contentColor = Color.White,
                modifier = Modifier.clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Beranda") },
                    selected = false,
                    onClick = { onNavigateBack() },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = HunterGreen,
                        selectedTextColor = Color.White,
                        indicatorColor = Color.White,
                        unselectedIconColor = Color.LightGray
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = "Mata Kuliah") },
                    selected = true,
                    onClick = { },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = HunterGreen,
                        selectedTextColor = Color.White,
                        indicatorColor = Color.White,
                        unselectedIconColor = Color.LightGray
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.BookmarkBorder, contentDescription = "Bookmark") },
                    selected = false,
                    onClick = { },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Color.LightGray
                    )
                )
            }
        },
        containerColor = VanillaCream
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header: judul kiri, logout kanan
            // Header: profil kiri, logout kanan
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
                            .background(HunterGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.White)
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
                    onClick = onLogout,
                    modifier = Modifier
                        .background(BlushedBrick, CircleShape)
                        .size(40.dp)
                ) {
                    Icon(
                        Icons.Default.ExitToApp,
                        contentDescription = "Logout",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Cari mata kuliah", color = Color.Gray) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray)
                },
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
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Deskripsi singkat
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(20.dp)
                        .background(Color.Black)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Akses cepat Mata Kuliah melalui kolom pencarian",
                    fontSize = 13.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(20.dp)
                        .background(Color.Black)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section label dengan icon buku
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.AutoMirrored.Filled.MenuBook,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Jelajah Mata Kuliah",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Content
            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = HunterGreen)
                    }
                }
                error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = error, color = MaterialTheme.colorScheme.error)
                    }
                }
                matkulList.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Tidak ada mata kuliah ditemukan.", color = Color.Gray)
                    }
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(matkulList) { matkul ->
                            MatkulCard(
                                matkul = matkul,
                                onLihatSelengkapnya = { onNavigateToDetail(matkul.id_matkul) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MatkulCard(
    matkul: Matkul,
    onLihatSelengkapnya: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SageGreen),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.height(260.dp)) {
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
                Text(
                    text = matkul.nama_matkul,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text("Tingkat kesulitan", fontSize = 10.sp, color = Color.Black)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = StarYellow,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "${matkul.tingkat_kesulitan}/5",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    "${matkul.arsip} arsip (materi, tugas, soal)",
                    fontSize = 9.sp,
                    color = Color.Black.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onLihatSelengkapnya,
                    colors = ButtonDefaults.buttonColors(containerColor = YellowGreen),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                ) {
                    Text(
                        "Lihat Selengkapnya",
                        color = Color.Black,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MataKuliahScreenPreview() {
    StudyScopeTheme {
        MataKuliahContent(
            username = "nama pengguna",
            searchQuery = "",
            onSearchQueryChange = {},
            isLoading = false,
            error = null,
            matkulList = listOf(
                Matkul(1, "Algoritma & Pemrograman", 4.5, 10),
                Matkul(2, "Struktur Data", 4.0, 8),
                Matkul(3, "Basis Data", 3.5, 12),
                Matkul(4, "Jaringan Komputer", 4.2, 6),
            ),
            onNavigateBack = {},
            onNavigateToDetail = {}
        )
    }
}