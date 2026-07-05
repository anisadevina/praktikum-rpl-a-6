package com.example.studyscope.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary          = HunterGreen,       // Hijau tua
    onPrimary        = White,
    primaryContainer = SageGreen,
    onPrimaryContainer = White,

    secondary        = YellowGreen,       // Kuning-Hijau
    onSecondary      = Text,
    secondaryContainer = YellowGreen,
    onSecondaryContainer = Text,

    tertiary         = BlueBottomOn,      // Biru (untuk tombol utama/CTA)
    onTertiary       = White,

    error            = BlushedBrick,      // Merah bata
    onError          = White,

    background       = VanillaCream,      // Background krem terang
    onBackground     = Text,

    surface          = White,
    onSurface        = Text,
    surfaceVariant   = VanillaCream,
    onSurfaceVariant = TextSecondary,

    outline          = Border,
    outlineVariant   = Border,
)

private val DarkColorScheme = darkColorScheme(
    primary          = SageGreen,
    onPrimary        = White,
    primaryContainer = HunterGreen,
    onPrimaryContainer = White,
    secondary        = YellowGreen,
    onSecondary      = Text,
    tertiary         = BlueBottomOn,
    onTertiary       = White,
    error            = BlushedBrick,
    onError          = White,
    background       = Color(0xFF1A1A1A),
    onBackground     = White,
    surface          = Color(0xFF2A2A2A),
    onSurface        = White,
)

object AppSpacing {
    val xs  : Dp = 4.dp
    val sm  : Dp = 8.dp
    val md  : Dp = 16.dp
    val lg  : Dp = 24.dp
    val xl  : Dp = 32.dp
    val xxl : Dp = 48.dp
}

@Composable
fun StudyScopeTheme(
    // Kita set default ke false untuk mengabaikan sistem dark mode
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    // Memaksa menggunakan LightColorScheme agar estetika tetap konsisten (Cream & Green)
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = AppTypography,
        content     = content,
    )
}
