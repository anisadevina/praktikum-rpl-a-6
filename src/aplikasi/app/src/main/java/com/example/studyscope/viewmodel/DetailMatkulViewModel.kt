package com.example.studyscope.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyscope.model.DetailMatkulData
import com.example.studyscope.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailMatkulViewModel : ViewModel() {

    private val _detailData = MutableStateFlow<DetailMatkulData?>(null)
    val detailData: StateFlow<DetailMatkulData?> = _detailData

    private val _username = MutableStateFlow("Memuat...")
    val username: StateFlow<String> = _username

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchDetail(token: String, idMatkul: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = ApiClient.instance.getDetailMatkul("Bearer $token", idMatkul)
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    _detailData.value = data

                    _username.value = data?.user?.username ?: "Pengguna"
                } else {
                    _error.value = "Gagal memuat data"
                }
            } catch (e: Exception) {
                _error.value = "Terjadi kesalahan: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun toggleBookmark(token: String, idMatkul: Int, idDokumen: Int) {
        viewModelScope.launch {
            try {
                // Tembak API Laravel-nya
                val response = ApiClient.instance.toggleBookmark("Bearer $token", idDokumen)

                if (response.isSuccessful) {
                    // Jika sukses, refresh data di layar agar ikon bookmark-nya berubah
                    fetchDetail(token, idMatkul)
                } else {
                    _error.value = "Gagal mengubah status bookmark."
                }
            } catch (e: Exception) {
                _error.value = "Koneksi bermasalah saat menyimpan arsip."
            }
        }
    }
}