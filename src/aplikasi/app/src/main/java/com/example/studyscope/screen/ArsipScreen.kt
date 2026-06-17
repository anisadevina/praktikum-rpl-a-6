package com.example.studyscope.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FileCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studyscope.ui.theme.AppSpacing
import com.example.studyscope.ui.theme.StudyScopeTheme

data class ArsipItem(
    val title: String,
    val year: String,
    val uploadDate: String
)

@Composable
fun ArsipScreen(
    onNavigateToBeranda: () -> Unit = {},
    onNavigateToLibrary: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val arsipList = remember {
        listOf(
            ArsipItem("Soal UAS Kalkulus II", "2024", "Diunggah pada 23 Mei 2026"),
            ArsipItem("Soal UAS Organisasi Sistem Komputer", "2024", "Diunggah pada 23 Mei 2026"),
            ArsipItem("Soal UAS Organisasi Sistem Komputer", "2024", "Diunggah pada 23 Mei 2026"),
            ArsipItem("Soal UAS Organisasi Sistem Komputer", "2024", "Diunggah pada 23 Mei 2026"),
            ArsipItem("Soal UAS Organisasi Sistem Komputer", "2024", "Diunggah pada 23 Mei 2026"),
            ArsipItem("Soal UAS Organisasi Sistem Komputer", "2024", "Diunggah pada 23 Mei 2026"),
            ArsipItem("Soal UAS Organisasi Sistem Komputer", "2024", "Diunggah pada 23 Mei 2026"),
            ArsipItem("Soal UAS Organisasi Sistem Komputer", "2024", "Diunggah pada 23 Mei 2026")
        )
    }

    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentRoute = "arsip",
                onNavigateToBeranda = onNavigateToBeranda
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(vertical = AppSpacing.md),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = Color.White,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(AppSpacing.sm))
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = "nama pengguna",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }

                IconButton(
                    onClick = onLogout,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.error, CircleShape)
                        .size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Logout",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { 
                    Text(
                        "Cari mata kuliah", 
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    ) 
                },
                leadingIcon = { 
                    Icon(
                        Icons.Default.Search, 
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(20.dp)
                    ) 
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(25.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    disabledBorderColor = Color.Transparent,
                    errorBorderColor = Color.Transparent,
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(AppSpacing.md))

            Text(
                text = "| Akses cepat Mata Kuliah melalui kolom pencarian |",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(AppSpacing.md))

            // Section Title
            Row(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.BookmarkBorder,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(AppSpacing.sm))
                Text(
                    text = "Temukan Arsip Mata Kuliah",
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            Spacer(modifier = Modifier.height(AppSpacing.md))

            // List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = AppSpacing.md)
            ) {
                items(arsipList) { item ->
                    ArsipCard(item)
                }
            }
        }
    }
}

@Composable
fun ArsipCard(item: ArsipItem) {
    Surface(
        color = MaterialTheme.colorScheme.secondary,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(AppSpacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.FileCopy,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.onSecondary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = item.year,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = item.uploadDate,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.7f)
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.Bookmark,
                contentDescription = "Bookmarked",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onNavigateToBeranda: () -> Unit = {},
    onNavigateToArsip: () -> Unit = {}
) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Beranda Icon
            if (currentRoute == "beranda") {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.White, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Inventory2,
                        contentDescription = "Beranda",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            } else {
                IconButton(onClick = onNavigateToBeranda) {
                    Icon(
                        imageVector = Icons.Default.Inventory2,
                        contentDescription = "Beranda",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            // Library Icon
            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                    contentDescription = "Library",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            // Archive Icon
            if (currentRoute == "arsip") {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.White, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = "Arsip",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            } else {
                IconButton(onClick = onNavigateToArsip) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = "Arsip",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArsipScreenPreview() {
    StudyScopeTheme {
        ArsipScreen()
    }
}
