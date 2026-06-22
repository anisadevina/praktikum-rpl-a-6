package com.example.studyscope.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.studyscope.ui.theme.HunterGreen
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPDFReader
import com.rizzi.bouquet.rememberVerticalPdfReaderState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfViewerScreen(
    kodeRahasia: String,
    token: String,
    onNavigateBack: () -> Unit
) {
    // 1. Tembak langsung ke API Laravel-mu
    val pdfUrl = "http://10.49.126.103:8000/api/arsip/view/$kodeRahasia"

    // 2. Bouquet akan mengurus proses unduh dan render secara otomatis
    val pdfState = rememberVerticalPdfReaderState(
        resource = ResourceType.Remote(
            url = pdfUrl,
            headers = hashMapOf("Authorization" to "Bearer $token") // Kirim karcis masuknya!
        ),
        isZoomEnable = true
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lihat Dokumen", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {

            // 3. Kanvas Pembaca PDF
            VerticalPDFReader(
                state = pdfState,
                modifier = Modifier.fillMaxSize()
            )

            // 4. Animasi Loading saat dokumen sedang diproses
            if (!pdfState.isLoaded && pdfState.error == null) {
                CircularProgressIndicator(
                    color = HunterGreen,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // 5. Pesan error jika gagal
            if (pdfState.error != null) {
                Text(
                    text = "Gagal memuat dokumen",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}