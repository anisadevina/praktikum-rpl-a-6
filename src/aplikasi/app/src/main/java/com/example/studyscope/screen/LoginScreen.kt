package com.example.studyscope.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studyscope.ui.theme.AppSpacing
import com.example.studyscope.ui.theme.StudyScopeTheme
import com.example.studyscope.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = viewModel(),
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: (String, String) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val authState by viewModel.authState.collectAsState()
    val authToken by viewModel.token.collectAsState()
    val savedUsername by viewModel.username.collectAsState()

    if (authState == "SuccessLogin" && authToken != null && savedUsername != null) {
        LaunchedEffect(Unit) {
            onLoginSuccess(authToken!!, savedUsername!!)
            viewModel.resetState()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            Box(
                modifier = Modifier
                    .size(width = 180.dp, height = 90.dp)
                    .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = "Logo",
                    modifier = Modifier.size(36.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(AppSpacing.lg))

            Text(
                text = "Selamat Datang di\nStudy Scope",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(AppSpacing.xl))

            // Field Username
            Text(
                text = "Username",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                placeholder = { Text("Masukkan Username", color = Color.LightGray) },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { 
                    Icon(Icons.Default.Person, null, modifier = Modifier.size(20.dp)) 
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(AppSpacing.md))

            // Field Password
            Text(
                text = "Password",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("*******", color = Color.LightGray) },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { 
                    Icon(Icons.Default.Lock, null, modifier = Modifier.size(20.dp))
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(AppSpacing.xl))

            Button(
                onClick = { viewModel.login(username, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                ),
                enabled = authState != "Loading"
            ) {
                Text(
                    text = if (authState == "Loading") "Memuat..." else "Masuk",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White
                )
            }

            if (authState.startsWith("Error") || authState.startsWith("Gagal")) {
                Text(
                    text = authState,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            TextButton(onClick = {
                viewModel.resetState()
                onNavigateToRegister()
            }) {
                Text(
                    text = buildAnnotatedString {
                        append("Belum memiliki akun? ")
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.tertiary, fontWeight = FontWeight.Bold)) {
                            append("Daftar")
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_5")
@Composable
fun LoginScreenPreview() {
    StudyScopeTheme {
        LoginScreen(
            onNavigateToRegister = {},
            onLoginSuccess = { _, _ -> }
        )
    }
}
