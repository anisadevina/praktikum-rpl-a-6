package com.example.studyscope.model

data class LoginRequest(
    val username: String,
    val password: String
)

data class RegisterRequest(
    val nim: String,
    val username: String,
    val email_user: String,
    val password: String
)

data class AuthResponse(
    val status: String,
    val message: String,
    val token: String? = null // Nanti diisi token Sanctum
)