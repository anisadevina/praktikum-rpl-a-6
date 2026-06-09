package com.example.studyscope.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyscope.model.BerandaData
import com.example.studyscope.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BerandaViewModel : ViewModel() {

    // Menyimpan data beranda yang didapat dari API
    private val _berandaData = MutableStateFlow<BerandaData?>(null)
    val berandaData: StateFlow<BerandaData?> = _berandaData

    // Status loading saat API sedang ditarik
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Menyimpan pesan error jika gagal
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // State untuk fitur Search Bar
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        // Nanti logika filter datanya bisa dimasukkan ke sini
    }

    // Fungsi utama menembak API
    fun fetchBeranda(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                // Pastikan formatnya "Bearer [token]"
                val response = ApiClient.instance.getBeranda("Bearer $token")

                if (response.isSuccessful && response.body()?.status == "success") {
                    _berandaData.value = response.body()?.data
                } else {
                    _error.value = "Gagal memuat data beranda."
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = "Koneksi bermasalah. Cek jaringanmu."
            } finally {
                _isLoading.value = false
            }
        }
    }
}