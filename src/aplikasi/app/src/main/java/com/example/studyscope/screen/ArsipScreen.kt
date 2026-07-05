package com.example.studyscope.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.outlined.BookmarkBorder
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.text.style.TextOverflow
import com.example.studyscope.model.ArsipItem
import com.example.studyscope.ui.theme.AppSpacing
import com.example.studyscope.ui.theme.BlushedBrick
import com.example.studyscope.ui.theme.SageGreen
import com.example.studyscope.ui.theme.StudyScopeTheme
import com.example.studyscope.viewmodel.ArsipViewModel

@Composable
fun ArsipScreen(
    token: String = "",
    username: String = "nama pengguna",
    onNavigateToBeranda: () -> Unit = {},
    onNavigateToLibrary: () -> Unit = {},
    onOpenDokumen: (String) -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: ArsipViewModel = viewModel()
) {
    val arsipList   by viewModel.filteredArsip.collectAsState()
    val isLoading   by viewModel.isLoading.collectAsState()
    val error       by viewModel.error.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchArsip(token)
    }

    ArsipContent(
        username = username,
        searchQuery = searchQuery,
        onSearchQueryChange = { viewModel.updateSearchQuery(it) },
        onSearchAction = { viewModel.fetchArsip(token) },
        isLoading = isLoading,
        error = error,
        arsipList = arsipList,
        onNavigateToBeranda = onNavigateToBeranda,
        onNavigateToLibrary = onNavigateToLibrary,
        onOpenDokumen = onOpenDokumen,
        onBookmarkClick = { idDokumen -> viewModel.toggleBookmark(token, idDokumen) },
        onLogout = onLogout
    )
}

@Composable
fun ArsipContent(
    username: String,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchAction: () -> Unit,
    isLoading: Boolean,
    error: String?,
    arsipList: List<ArsipItem>,
    onNavigateToBeranda: () -> Unit,
    onNavigateToLibrary: () -> Unit,
    onOpenDokumen: (String) -> Unit,
    onBookmarkClick: (Int) -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentRoute = "arsip",
                onNavigateToBeranda = onNavigateToBeranda,
                onNavigateToLibrary = onNavigateToLibrary
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = AppSpacing.lg)
        ) {
            Spacer(modifier = Modifier.height(AppSpacing.md))
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
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = "Profile", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                    Spacer(modifier = Modifier.width(AppSpacing.sm))
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
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

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = {
                    Text(
                        "Cari judul file",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = { onSearchAction() }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp)
                    .background(Color.Transparent)
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(AppSpacing.sm))

            // Banner Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier
                    .width(2.dp)
                    .height(20.dp)
                    .background(MaterialTheme.colorScheme.primary))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Akses cepat Arsip File melalui kolom pencarian",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier
                    .width(2.dp)
                    .height(20.dp)
                    .background(MaterialTheme.colorScheme.primary))
            }

            Spacer(modifier = Modifier.height(AppSpacing.lg))

            // Section Title
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.BookmarkBorder,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(AppSpacing.sm))
                Text(
                    text = "Temukan Arsip Mata Kuliah",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Spacer(modifier = Modifier.height(AppSpacing.md))

            // List
            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
                error != null -> {
                    Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = AppSpacing.lg),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                arsipList.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Belum ada dokumen yang disimpan.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = AppSpacing.lg),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = AppSpacing.md)
                    ) {
                        items(arsipList) { item ->
                            ArsipCard(
                                item = item,
                                onBookmarkClick = { onBookmarkClick(item.idDokumen) },
                                onCardClick = {
                                    val kodeRahasia = item.fileUrl.substringAfterLast("/")
                                    onOpenDokumen(kodeRahasia)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ArsipCard(
    item: ArsipItem,
    onBookmarkClick: () -> Unit = {},
    onCardClick: () -> Unit = {}
) {
    Surface(
        color = SageGreen,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clickable { onCardClick() }
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
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.judul,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = item.namaMatkul,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = BlushedBrick,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = item.tahunDokumen.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Diunggah: ${item.waktuUnggahFormatted}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
            IconButton(onClick = onBookmarkClick, modifier = Modifier.size(32.dp)) {
                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = "Bookmarked",
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onNavigateToBeranda: () -> Unit = {},
    onNavigateToLibrary: () -> Unit = {},
    onNavigateToArsip: () -> Unit = {}
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier.clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Beranda") },
            selected = currentRoute == "beranda",
            onClick = onNavigateToBeranda,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                indicatorColor = MaterialTheme.colorScheme.onPrimary,
                unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = "Mata Kuliah") },
            selected = currentRoute == "matakuliah",
            onClick = onNavigateToLibrary,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                indicatorColor = MaterialTheme.colorScheme.onPrimary,
                unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
            )
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = if (currentRoute == "arsip") Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Arsip"
                )
            },
            selected = currentRoute == "arsip",
            onClick = onNavigateToArsip,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                indicatorColor = MaterialTheme.colorScheme.onPrimary,
                unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
            )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ArsipScreenPreview() {
    StudyScopeTheme {
        ArsipContent(
            username = "nama pengguna",
            searchQuery = "",
            onSearchQueryChange = {},
            onSearchAction = {},
            isLoading = false,
            error = null,
            arsipList = listOf(
                ArsipItem(
                    idDokumen = 1,
                    judul = "Soal UAS OSK",
                    kategoriFile = "soal",
                    tahunDokumen = 2024,
                    waktuUnggah = "2024-01-01",
                    filePath = "",
                    namaMatkul = "OSK",
                    namaDosen = "Dosen A",
                    waktuUnggahFormatted = "1 Jan 2024",
                    fileUrl = "url/abc"
                ),
                ArsipItem(
                    idDokumen = 2,
                    judul = "Materi Basis Data",
                    kategoriFile = "materi",
                    tahunDokumen = 2023,
                    waktuUnggah = "2023-12-01",
                    filePath = "",
                    namaMatkul = "Basdat",
                    namaDosen = "Dosen B",
                    waktuUnggahFormatted = "1 Des 2023",
                    fileUrl = "url/def"
                )
            ),
            onNavigateToBeranda = {},
            onNavigateToLibrary = {},
            onOpenDokumen = {},
            onBookmarkClick = {},
            onLogout = {}
        )
    }
}
