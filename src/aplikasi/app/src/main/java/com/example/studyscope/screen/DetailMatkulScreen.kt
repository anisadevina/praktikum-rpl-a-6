package com.example.studyscope.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.FileCopy
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
        onToggleBookmark = { idDokumen ->
            viewModel.toggleBookmark(token, idMatkul, idDokumen)
        },
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
    onToggleBookmark: (Int) -> Unit,
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
                .padding(horizontal = 24.dp)
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
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(top = 100.dp),
                            contentAlignment = Alignment.Center
                        ) {
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
                                    // Tingkat Kesulitan
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

                                    // Jumlah Arsip
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

                        // Section Arsip - border + judul (gaya kamu)
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
                                            onClick = { onOpenDokumen(dokumen.kodeRahasia) },
                                        ) { onToggleBookmark(dokumen.id_dokumen) }
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
    onClick: () -> Unit = {},
    onToggleBookmark: () -> Unit = {}
) {
    Surface(
        color = YellowGreen,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
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
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = HunterGreen,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "${dokumen.tahun_dokumen}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Diunggah pada ${formatTanggal(dokumen.waktu_unggah)}",
                        style = MaterialTheme.typography.bodySmall,
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

fun formatTanggal(tanggal: String): String {
    return try {
        val tanggalSaja = tanggal.substringBefore(" ")
        val parts = tanggalSaja.split("-")
        val bulanIndo = listOf(
            "Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"
        )
        val tahun = parts[0]
        val bulan = bulanIndo[parts[1].toInt() - 1]
        val hari = parts[2].toInt()
        "$hari $bulan $tahun"
    } catch (e: Exception) {
        tanggal
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
            onOpenDokumen = {},
            onToggleBookmark = {}
        )
    }
}