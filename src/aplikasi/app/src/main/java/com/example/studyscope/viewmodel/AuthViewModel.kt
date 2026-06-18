package com.example.studyscope.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyscope.model.LoginRequest
import com.example.studyscope.model.RegisterRequest
import com.example.studyscope.model.ValidationErrorResponse
import com.example.studyscope.network.ApiClient
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _authState = MutableStateFlow("Idle")
    val authState: StateFlow<String> = _authState

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    private val _fieldErrors = MutableStateFlow<Map<String, String>>(emptyMap())
    val fieldErrors: StateFlow<Map<String, String>> = _fieldErrors

    fun login(username: String, pass: String) {
        viewModelScope.launch {
            _authState.value = "Loading"
            _fieldErrors.value = emptyMap() // Bersihkan error lama

            try {
                val response = ApiClient.instance.login(LoginRequest(username, pass))
                if (response.isSuccessful && response.body()?.status == "success") {
                    _token.value = response.body()?.token
                    _authState.value = "SuccessLogin"
                } else if (response.code() == 422) {
                    parseValidationErrors(response.errorBody()?.string())
                    _authState.value = "ValidationError"
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
            _fieldErrors.value = emptyMap()

            try {
                val request = RegisterRequest(nim, username, email, pass)
                val response = ApiClient.instance.register(request)

                if (response.isSuccessful && response.body()?.status == "success") {
                    _token.value = response.body()?.token
                    _authState.value = "SuccessRegister"
                } else if (response.code() == 422) {
                    parseValidationErrors(response.errorBody()?.string())
                    _authState.value = "ValidationError"
                } else {
                    _authState.value = "Error: Terjadi kesalahan pada server"
                }
            } catch (e: Exception) {
                _authState.value = "Error: Gagal terhubung ke server"
            }
        }
    }

    private fun parseValidationErrors(errorJson: String?) {
        if (errorJson != null) {
            try {
                val parsedError = Gson().fromJson(errorJson, ValidationErrorResponse::class.java)
                // Mengambil pesan error pertama untuk setiap kolom
                val mappedErrors = parsedError.errors.mapValues { it.value.first() }
                _fieldErrors.value = mappedErrors
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun resetState() {
        _authState.value = "Idle"
        _fieldErrors.value = emptyMap()
    }
}