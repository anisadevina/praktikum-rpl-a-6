package com.example.studyscope.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.studyscope.network.ApiClient
import com.example.studyscope.model.LoginRequest
import com.example.studyscope.model.RegisterRequest


class AuthViewModel : ViewModel() {

    // Menyimpan status UI (Idle, Loading, Success, Error)
    private val _authState = MutableStateFlow("Idle")
    val authState: StateFlow<String> = _authState

    // Menyimpan token jika berhasil
    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    fun login(username: String, pass: String) {
        viewModelScope.launch {
            _authState.value = "Loading"
            try {
                val response = ApiClient.instance.login(LoginRequest(username, pass))
                if (response.isSuccessful && response.body()?.status == "success") {
                    _token.value = response.body()?.token
                    _authState.value = "SuccessLogin"
                } else {
                    _authState.value = "Error: Username atau Password salah"
                }
            } catch (e: Exception) {
                _authState.value = "Error: Gagal terhubung ke server"
            }
        }
    }

    fun register(nim: String, username: String, email: String, pass: String) {
        viewModelScope.launch {
            _authState.value = "Loading"
            try {
                val request = RegisterRequest(nim, username, email, pass)
                val response = ApiClient.instance.register(request)

                if (response.isSuccessful && response.body()?.status == "success") {
                    _token.value = response.body()?.token
                    _authState.value = "SuccessRegister"
                } else {
                    // Laravel biasanya mengirim error 422 jika validasi gagal
                    _authState.value = "Error: Cek kembali data (NIM/Email SSO)"
                }
            } catch (e: Exception) {
                _authState.value = "Error: Gagal terhubung ke server"
            }
        }
    }

    fun resetState() {
        _authState.value = "Idle"
    }
}