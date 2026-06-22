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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studyscope.ui.theme.HunterGreen
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.style.TextOverflow
import com.example.studyscope.ui.theme.*
import com.example.studyscope.model.Matkul
import com.example.studyscope.viewmodel.BerandaViewModel

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
    matkulList: List<Matkul>,
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
                        unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = "Mata Kuliah") },
                    selected = false,
                    onClick = { onNavigateToMatkul("") },
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f))
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.BookmarkBorder, contentDescription = "Arsip") },
                    selected = false,
                    onClick = onNavigateToArsip,
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f))
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

            // --- HEADER ---
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
                    onClick = onLogoutClick,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.error, CircleShape)
                        .size(40.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout", tint = MaterialTheme.colorScheme.onError)
                }
            }

            Spacer(modifier = Modifier.height(AppSpacing.md))

            // --- SEARCH BAR ---
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Cari mata kuliah", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp)
                    .background(Color.Transparent)
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium,
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

            Spacer(modifier = Modifier.height(AppSpacing.md))

            // --- HERO CARD ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Halo, $username!",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Selamat datang di Study Scope",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        text = "Ayo mulai perjalananmu dengan mengakses menu mata kuliah dan arsip",
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(AppSpacing.lg))
            Text(text = "Akses Terakhir Mata Kuliah", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(AppSpacing.md))

            // --- GRID KARTU MATA KULIAH ---
            if (isLoading) {
                Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
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
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(matkulList) { matkul ->
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
                                colors = CardDefaults.cardColors(containerColor = SageGreen),
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
                                            tint = SageGreen,
                                            modifier = Modifier.size(48.dp)
                                        )
                                    }

                                    Column(modifier = Modifier
                                        .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 12.dp)
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
                                            onClick = { onNavigateToDetail(matkul.id_matkul) },
                                            colors = ButtonDefaults.buttonColors(containerColor = YellowGreen),
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