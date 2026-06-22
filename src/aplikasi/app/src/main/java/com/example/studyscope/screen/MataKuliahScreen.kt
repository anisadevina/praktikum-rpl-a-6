package com.example.studyscope.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.text.style.TextOverflow
import com.example.studyscope.model.Matkul
import com.example.studyscope.ui.theme.AppSpacing
import com.example.studyscope.ui.theme.StarYellow
import com.example.studyscope.ui.theme.StudyScopeTheme
import com.example.studyscope.viewmodel.MataKuliahViewModel

@Composable
fun MataKuliahScreen(
    token: String,
    initialQuery: String = "",
    onNavigateToBeranda: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToArsip: () -> Unit,
    onLogout: () -> Unit = {},
    viewModel: MataKuliahViewModel = viewModel()
) {
    val matkulList by viewModel.matkulList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isPaginating by viewModel.isPaginating.collectAsState()
    val error by viewModel.error.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val currentUsername by viewModel.username.collectAsState()

    LaunchedEffect(Unit) {
        if (initialQuery.isNotBlank()) {
            viewModel.updateSearchQuery(initialQuery, token)
        } else {
            viewModel.fetchMatkul(token = token)
        }
    }

    MataKuliahContent(
        username = currentUsername,
        searchQuery = searchQuery,
        onSearchQueryChange = { query -> viewModel.updateSearchQuery(query, token) },
        isLoading = isLoading,
        isPaginating = isPaginating,
        onLoadMore = { viewModel.loadMore(token) },
        error = error,
        matkulList = matkulList,
        onNavigateToBeranda = onNavigateToBeranda,
        onNavigateToDetail = onNavigateToDetail,
        onNavigateToArsip = onNavigateToArsip,
        onLogout = onLogout
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MataKuliahContent(
    username: String,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    isLoading: Boolean,
    isPaginating: Boolean,
    onLoadMore: () -> Unit,
    error: String?,
    matkulList: List<Matkul>,
    onNavigateToBeranda: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToArsip: () -> Unit,
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
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.onPrimary,
                        unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Cari mata kuliah", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.onSurfaceVariant) },
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
                textStyle = MaterialTheme.typography.bodyMedium,
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
                    text = "Akses cepat Mata Kuliah melalui kolom pencarian",
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

            // Title
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Jelajah Mata Kuliah", style = MaterialTheme.typography.titleLarge)
            }

            Spacer(modifier = Modifier.height(AppSpacing.md))

            // Content
            when {
                isLoading && matkulList.isEmpty() -> {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
                error != null && matkulList.isEmpty() -> {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), contentAlignment = Alignment.Center) {
                        Text(text = error, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
                    }
                }
                matkulList.isEmpty() -> {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), contentAlignment = Alignment.Center) {
                        Text("Tidak ada mata kuliah ditemukan.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        itemsIndexed(matkulList) { index, matkul ->
                            if (index == matkulList.size - 1) {
                                LaunchedEffect(matkulList.size) { onLoadMore() }
                            }
                            MatkulCard(
                                matkul = matkul,
                                onLihatSelengkapnya = { onNavigateToDetail(matkul.id_matkul) }
                            )
                        }

                        if (isPaginating) {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Box(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(AppSpacing.md), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                                }
                            }
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
    val icons = listOf(
        Icons.AutoMirrored.Filled.MenuBook,
        Icons.Default.Code,
        Icons.Default.Terminal,
        Icons.Default.Storage,
        Icons.Default.Calculate,
        Icons.Default.Science,
        Icons.Default.LibraryBooks
    )
    val matkulIcon = icons[matkul.id_matkul % icons.size]

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = com.example.studyscope.ui.theme.SageGreen),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = matkulIcon,
                    contentDescription = null,
                    tint = com.example.studyscope.ui.theme.SageGreen,
                    modifier = Modifier.size(48.dp)
                )
            }
            Column(modifier = Modifier
                .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 10.dp)
                .fillMaxWidth()
                .weight(1f)) {
                Text(
                    text = matkul.nama_matkul,
                    color = Color.White,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        lineHeight = 18.sp
                    ),
                    maxLines = 2,
                    minLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tingkat kesulitan",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = StarYellow,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${matkul.tingkat_kesulitan}/5",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
                Text(
                    text = "(${matkul.arsip} arsip tersedia)",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = onLihatSelengkapnya,
                    colors = ButtonDefaults.buttonColors(containerColor = com.example.studyscope.ui.theme.YellowGreen),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Text(
                        "Lihat",
                        color = Color.Black,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
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
            isPaginating = false,
            onLoadMore = {},
            error = null,
            matkulList = listOf(
                Matkul(1, "Algoritma & Pemrograman", 4.5, 10),
                Matkul(2, "Struktur Data", 4.0, 8),
                Matkul(3, "Basis Data", 3.5, 12),
                Matkul(4, "Jaringan Komputer", 4.2, 6),
            ),
            onNavigateToBeranda = {},
            onNavigateToDetail = {},
            onNavigateToArsip = {},
            onLogout = {}
        )
    }
}
