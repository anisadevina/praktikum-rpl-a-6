package com.example.studyscope.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studyscope.model.DetailMatkulData
import com.example.studyscope.model.Dokumen
import com.example.studyscope.model.MatkulDetail
import com.example.studyscope.model.UserData
import com.example.studyscope.ui.theme.*
import com.example.studyscope.viewmodel.DetailMatkulViewModel

@Composable
fun DetailMatkulScreen(
    token: String,
    idMatkul: Int,
    onNavigateBack: () -> Unit,
    onNavigateToBeranda: () -> Unit,
    onNavigateToArsip: () -> Unit,   
    onOpenDokumen: (String) -> Unit,
    onLogout: () -> Unit = {},
    viewModel: DetailMatkulViewModel = viewModel()
) {
    val detailData by viewModel.detailData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val currentUsername by viewModel.username.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchDetail(token, idMatkul)
    }

    DetailMatkulContent(
        username = currentUsername,
        detailData = detailData,
        isLoading = isLoading,
        error = error,
        onNavigateBack = onNavigateBack,
        onNavigateToBeranda = onNavigateToBeranda,
        onNavigateToArsip = onNavigateToArsip,
        onOpenDokumen = onOpenDokumen,
        onLogout = onLogout
    )
}

@Composable
fun DetailMatkulContent(
    username: String,
    detailData: DetailMatkulData?,
    isLoading: Boolean,
    error: String?,
    onNavigateBack: () -> Unit,
    onNavigateToBeranda: () -> Unit,
    onNavigateToArsip: () -> Unit,
    onOpenDokumen: (String) -> Unit,
    onLogout: () -> Unit = {}
) {
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
                    onClick = onNavigateToBeranda,
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.LightGray)
                )
                NavigationBarItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = "Mata Kuliah") },
                    selected = true,
                    onClick = { },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = HunterGreen,
                        indicatorColor = Color.White,
                        unselectedIconColor = Color.LightGray
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.BookmarkBorder, contentDescription = "Arsip") },
                    selected = false,
                    onClick = onNavigateToArsip,
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.LightGray)
                )
            }
        },
        containerColor = VanillaCream
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))

                // Header Top Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(40.dp).clip(CircleShape).background(HunterGreen),
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
                        modifier = Modifier.background(BlushedBrick, CircleShape).size(40.dp)
                    ) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tombol Kembali
                TextButton(onClick = onNavigateBack, contentPadding = PaddingValues(0.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.Black)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Kembali", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            // --- KONTEN UTAMA ---
            when {
                isLoading -> {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(top = 100.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = HunterGreen)
                        }
                    }
                }
                error != null -> {
                    item { Text(text = error, color = MaterialTheme.colorScheme.error) }
                }
                detailData != null -> {
                    val matkul = detailData.matkul

                    item {
                        // Kotak Info Hijau Tua
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = HunterGreen)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(
                                    text = matkul.nama_matkul.uppercase(),
                                    color = Color.White,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = matkul.deskripsi ?: "-",
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.height(16.dp))


                                Row(
                                    modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // 1. Tingkat Kesulitan
                                    Card(
                                        modifier = Modifier.weight(1f).fillMaxHeight(),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(10.dp).fillMaxSize(),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text("Tingkat Kesulitan", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Row(verticalAlignment = Alignment.Bottom) {
                                                Text("${matkul.tingkat_kesulitan}", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                                Text("/5.0", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 4.dp))
                                            }
                                            Text(detailData.teksKesulitan, fontSize = 9.sp, color = Color.Gray, maxLines = 1)
                                        }
                                    }

                                    // 2. Jumlah Arsip
                                    Card(
                                        modifier = Modifier.weight(1f).fillMaxHeight(),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(10.dp).fillMaxSize(),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text("Jumlah Arsip", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text("${detailData.jumlahArsip}", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                            Text("Arsip tersimpan", fontSize = 9.sp, color = Color.Gray)
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Judul Daftar Arsip
                        Text(
                            text = "Arsip Mata Kuliah",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }

                    if (detailData.daftarArsip.isEmpty()) {
                        item {
                            Text("Belum ada dokumen untuk mata kuliah ini.", color = Color.Gray, fontSize = 14.sp)
                        }
                    } else {
                        items(detailData.daftarArsip) { dokumen ->
                            ArsipItem(
                                dokumen = dokumen,
                                onClick = { onOpenDokumen(dokumen.kodeRahasia) }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }

                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }
    }
}

@Composable
fun ArsipItem(dokumen: Dokumen, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SageGreen),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(40.dp).background(YellowGreen, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.ContentCopy, null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(dokumen.judul, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White, maxLines = 1)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = RoundedCornerShape(4.dp), color = HunterGreen) {
                        Text("${dokumen.tahun_dokumen}", fontSize = 10.sp, color = Color.White, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Diunggah ${dokumen.waktu_unggah.take(10)}", fontSize = 10.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
            IconButton(onClick = { }) {
                Icon(if (dokumen.is_bookmarked) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder, "Bookmark", tint = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailMatkulScreenPreview() {
    StudyScopeTheme {
        DetailMatkulContent(
            username = "nama pengguna",
            detailData = DetailMatkulData(
                user = UserData(1, "nama pengguna", "user"),
                matkul = MatkulDetail(1, "Pemrograman Web", "Deskripsi...", 4.2),
                teksKesulitan = "Cukup sulit",
                jumlahArsip = 3,
                daftarArsip = listOf(
                    Dokumen(1, "Soal UAS 2024", "soal ujian", 2024, "2024-05-23", false, "abc"),
                    Dokumen(2, "Materi Web", "materi", 2023, "2023-05-23", true, "def")
                )
            ),
            isLoading = false,
            error = null,
            onNavigateBack = {},
            onNavigateToBeranda = {},
            onNavigateToArsip = {},
            onOpenDokumen = {}
        )
    }
}