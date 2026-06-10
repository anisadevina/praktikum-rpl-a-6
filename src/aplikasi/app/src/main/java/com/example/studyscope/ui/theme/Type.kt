package com.example.studyscope.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.studyscope.R

val PlusJakartaSans = FontFamily(
    Font(R.font.plus_jakarta_sans_regular,    FontWeight.Normal),
    Font(R.font.plus_jakarta_sans_medium,     FontWeight.Medium),
    Font(R.font.plus_jakarta_sans_semibold,   FontWeight.SemiBold),
    Font(R.font.plus_jakarta_sans_bold,       FontWeight.Bold),
    Font(R.font.plus_jakarta_sans_extrabold,  FontWeight.ExtraBold),
)

val AppTypography = Typography(
    // Judul Utama (Lebih kecil dari sebelumnya: 28 -> 24)
    headlineLarge = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Bold,
        fontSize   = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.2).sp,
    ),

    // Judul Section (20 -> 18)
    headlineMedium = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Bold,
        fontSize   = 18.sp,
        lineHeight = 24.sp,
    ),

    // Label Input (PENTING: 16 -> 14 agar tidak terlihat raksasa)
    headlineSmall = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.SemiBold,
        fontSize   = 14.sp,
        lineHeight = 20.sp,
    ),

    // Teks Isi (14 -> 14 tetap, tapi line height disesuaikan)
    bodyMedium = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Normal,
        fontSize   = 14.sp,
        lineHeight = 20.sp,
    ),

    // Keterangan / Hint (13 -> 12)
    bodySmall = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Normal,
        fontSize   = 12.sp,
        lineHeight = 16.sp,
    ),

    // Tombol
    labelLarge = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Bold,
        fontSize   = 15.sp,
        lineHeight = 20.sp,
    ),
)
