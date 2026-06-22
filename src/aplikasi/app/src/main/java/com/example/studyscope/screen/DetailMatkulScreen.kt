package com.example.studyscope.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.FileCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Beranda") },
                    selected = false,
                    onClick = onNavigateToBeranda,
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                        indicatorColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = "Mata Kuliah") },
                    selected = true,
                    onClick = { },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.onPrimary,
                        unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.BookmarkBorder, contentDescription = "Arsip") },
                    selected = false,
                    onClick = onNavigateToArsip,
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                        indicatorColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = AppSpacing.lg)
        ) {
            item {
                Spacer(modifier = Modifier.height(AppSpacing.md))

                // Header Top Bar
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
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = "Profile", tint = MaterialTheme.colorScheme.onPrimary)
                        }
                        Spacer(modifier = Modifier.width(AppSpacing.sm))
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                        ) {
                            Text(
                                text = username,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                    IconButton(
                        onClick = onLogout,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.error, CircleShape)
                            .size(40.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout", tint = MaterialTheme.colorScheme.onError)
                    }
                }

                Spacer(modifier = Modifier.height(AppSpacing.md))

                // Tombol Kembali
                TextButton(onClick = onNavigateBack, contentPadding = PaddingValues(0.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = MaterialTheme.colorScheme.onBackground)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Kembali", color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                    }
                }

                Spacer(modifier = Modifier.height(AppSpacing.sm))
            }

            // --- KONTEN UTAMA ---
            when {
                isLoading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
                error != null -> {
                    item { Text(text = error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium) }
                }
                detailData != null -> {
                    val matkul = detailData.matkul

                    item {
                        // Kotak Info Hijau Tua
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(AppSpacing.md)) {
                                Text(
                                    text = matkul.nama_matkul.uppercase(),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = matkul.deskripsi ?: "Tidak ada deskripsi tersedia.",
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(AppSpacing.md))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(IntrinsicSize.Max),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    // Tingkat Kesulitan
                                    Card(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight(),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .padding(AppSpacing.sm)
                                                .fillMaxSize(),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text("Tingkat Kesulitan", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Row(verticalAlignment = Alignment.Bottom) {
                                                Text("${matkul.tingkat_kesulitan}", style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onSurfaceVariant)
                                                Text("/5.0", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f), modifier = Modifier.padding(bottom = 6.dp))
                                            }
                                            Text(
                                                text = detailData.teksKesulitan,
                                                style = MaterialTheme.typography.labelSmall.copy(lineHeight = 12.sp),
                                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                                textAlign = TextAlign.Center,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }

                                    // Jumlah Arsip
                                    Card(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight(),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .padding(AppSpacing.sm)
                                                .fillMaxSize(),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text("Jumlah Arsip", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text("${detailData.jumlahArsip}", style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            Text(
                                                text = "Arsip tersimpan",
                                                style = MaterialTheme.typography.labelSmall.copy(lineHeight = 12.sp),
                                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                                textAlign = TextAlign.Center,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(AppSpacing.lg))

                        // Section Arsip - border + judul (gaya kamu)
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = AppSpacing.md)) {
                                Text(
                                    text = "Arsip Mata Kuliah",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                                Spacer(modifier = Modifier.height(AppSpacing.md))
                                if (detailData.daftarArsip.isEmpty()) {
                                    Text(
                                        text = "Belum ada arsip untuk mata kuliah ini.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    )
                                } else {
                                    detailData.daftarArsip.forEach { dokumen ->
                                        ArsipItem(
                                            dokumen = dokumen,
                                            onClick = { onOpenDokumen(dokumen.kodeRahasia) },
                                        ) { onToggleBookmark(dokumen.id_dokumen) }
                                        Spacer(modifier = Modifier.height(AppSpacing.sm))
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(AppSpacing.md))
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
        color = SageGreen,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.FileCopy,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(20.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = dokumen.judul,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Diunggah pada ${formatTanggal(dokumen.waktu_unggah)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Black.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    color = BlushedBrick,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "${dokumen.tahun_dokumen}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            IconButton(onClick = onToggleBookmark, modifier = Modifier.size(32.dp)) {
                Icon(
                    imageVector = if (dokumen.is_bookmarked) Icons.Default.Bookmark
                    else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Bookmark",
                    modifier = Modifier.size(20.dp),
                    tint = Color.Black
                )
            }
        }
    }
}

fun formatTanggal(tanggal: String): String {
    return try {
        // Handle format ISO (YYYY-MM-DD)
        val cleanDate = tanggal.substringBefore(" ") // case if it has time
        val parts = cleanDate.split("-")
        if (parts.size < 3) return tanggal

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