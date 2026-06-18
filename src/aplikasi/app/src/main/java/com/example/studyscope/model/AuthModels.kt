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

data class AuthUserData(
    val id_user: Int,
    val username: String,
    val role: String
)

data class AuthResponse(
    val status: String,
    val message: String,
    val token: String? = null, // Nanti diisi token Sanctum
    val user: AuthUserData? = null  // Data user setelah login/register
)

data class ValidationErrorResponse(
    val message: String,
    val errors: Map<String, List<String>>
)