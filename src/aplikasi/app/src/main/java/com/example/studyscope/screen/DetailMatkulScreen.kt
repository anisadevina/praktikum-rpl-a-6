package com.example.studyscope.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material.icons.outlined.FileCopy

@Composable
fun DetailMatkulScreen(
    token: String,
    username: String,
    idMatkul: Int,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit = {},
    viewModel: DetailMatkulViewModel = viewModel()
) {
    val detailData by viewModel.detailData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchDetail(token, idMatkul)
    }

    DetailMatkulContent(
        username = username,
        detailData = detailData,
        isLoading = isLoading,
        error = error,
        onNavigateBack = onNavigateBack,
        onLogout = onLogout,
        onToggleBookmark = { idDokumen -> viewModel.toggleBookmark(token, idDokumen) }
    )
}

@Composable
fun DetailMatkulContent(
    username: String,
    detailData: DetailMatkulData?,
    isLoading: Boolean,
    error: String?,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit = {},
    onToggleBookmark: (Int) -> Unit = {}
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
                    onClick = { },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Color.LightGray
                    )
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))

                // Header
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

                // Search bar (non-functional, hanya tampilan)
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
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
                    enabled = false
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Konten utama
            when {
                isLoading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = HunterGreen)
                        }
                    }
                }
                error != null -> {
                    item {
                        Text(text = error, color = MaterialTheme.colorScheme.error)
                    }
                }
                detailData != null -> {
                    val matkul = detailData.matkul

                    item {
                        // Card info utama matkul
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

                                // 3 kotak info
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(IntrinsicSize.Max),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Fokus Materi
                                    Card(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight(),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White)
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Text(
                                                "Fokus Materi",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            listOf(
                                                "Fokus materi 1",
                                                "Fokus materi 2",
                                                "Fokus materi 3",
                                                "Fokus materi 4",
                                                "Fokus materi 5"
                                            ).forEach {
                                                Text("• $it", fontSize = 9.sp, color = Color.DarkGray)
                                            }
                                        }
                                    }

                                    // Tingkat Kesulitan
                                    Card(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight(),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .padding(10.dp)
                                                .fillMaxSize(),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                "Tingkat Kesulitan",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Row(verticalAlignment = Alignment.Bottom) {
                                                Text(
                                                    text = "${matkul.tingkat_kesulitan}",
                                                    fontSize = 28.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.Black
                                                )
                                                Text(
                                                    text = "/5.0",
                                                    fontSize = 12.sp,
                                                    color = Color.Gray,
                                                    modifier = Modifier.padding(bottom = 4.dp)
                                                )
                                            }
                                            Text(
                                                text = detailData.teksKesulitan,
                                                fontSize = 9.sp,
                                                color = Color.Gray
                                            )
                                        }
                                    }

                                    // Jumlah Arsip
                                    Card(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight(),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .padding(10.dp)
                                                .fillMaxSize(),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                "Jumlah Arsip",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = "${detailData.jumlahArsip}",
                                                fontSize = 28.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black
                                            )
                                            Text(
                                                "Arsip tersimpan",
                                                fontSize = 9.sp,
                                                color = Color.Gray
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Section Arsip
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color.Black)
                        ) {
                            Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)) {
                                Text(
                                    text = "Arsip Mata Kuliah",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                if (detailData.daftarArsip.isEmpty()) {
                                    Text(
                                        text = "Belum ada arsip untuk mata kuliah ini.",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                } else {
                                    detailData.daftarArsip.forEach { dokumen ->
                                        ArsipItem(
                                            dokumen = dokumen,
                                            onToggleBookmark = { onToggleBookmark(dokumen.id_dokumen) }
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ArsipItem(
    dokumen: Dokumen,
    onToggleBookmark: () -> Unit = {}
) {
    Surface(
        color = MaterialTheme.colorScheme.secondary,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.FileCopy,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = dokumen.judul,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "${dokumen.tahun_dokumen}",
                            fontSize = 10.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 1.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Diunggah pada ${dokumen.waktu_unggah}",
                        fontSize = 11.sp,
                        color = Color.Black.copy(alpha = 0.7f)
                    )
                }
            }
            IconButton(onClick = onToggleBookmark) {
                Icon(
                    imageVector = if (dokumen.is_bookmarked) Icons.Default.Bookmark
                    else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Bookmark",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DetailMatkulScreenPreview() {
    StudyScopeTheme {
        DetailMatkulContent(
            username = "nama pengguna",
            detailData = DetailMatkulData(
                user = UserData(1, "nama pengguna", "user"),
                matkul = MatkulDetail(1, "Pemrograman Web", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", 4.2),
                teksKesulitan = "Cukup sulit",
                jumlahArsip = 23,
                daftarArsip = listOf(
                    Dokumen(1, "Soal UAS Kalkulus II", "soal ujian", 2024, "23 Mei 2026", false),
                    Dokumen(2, "Soal UAS Organisasi Sistem Komputer", "soal ujian", 2024, "23 Mei 2026", true),
                    Dokumen(3, "Materi Pemrograman Web", "materi", 2024, "20 Mei 2026", false),
                )
            ),
            isLoading = false,
            error = null,
            onNavigateBack = {},
            onToggleBookmark = {}
        )
    }
}