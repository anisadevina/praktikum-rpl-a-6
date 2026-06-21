package com.example.studyscope.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyscope.model.ArsipItem
import com.example.studyscope.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArsipViewModel : ViewModel() {

    private val _arsipList = MutableStateFlow<List<ArsipItem>>(emptyList())
    val arsipList: StateFlow<List<ArsipItem>> = _arsipList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _filteredArsip = MutableStateFlow<List<ArsipItem>>(emptyList())
    val filteredArsip: StateFlow<List<ArsipItem>> = _filteredArsip

    fun fetchArsip(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                // Kirim query search ke API jika ada
                val query = _searchQuery.value.ifBlank { null }
                val response = ApiClient.instance.getArsip("Bearer $token", search = query)
                if (response.isSuccessful && response.body()?.status == "success") {
                    val list = response.body()?.data?.daftarArsip ?: emptyList()
                    _arsipList.value = list
                    _filteredArsip.value = list
                } else {
                    _error.value = "Gagal memuat data arsip."
                }
            } catch (e: Exception) {
                _error.value = "Koneksi bermasalah. Cek jaringanmu."
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Filter list arsip berdasarkan query pencarian
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        val all = _arsipList.value
        _filteredArsip.value = if (query.isBlank()) {
            all
        } else {
            all.filter { it.judul.contains(query, ignoreCase = true) }
        }
    }

    // Toggle bookmark — refresh list setelah berhasil
    fun toggleBookmark(token: String, idDokumen: Int) {
        viewModelScope.launch {
            try {
                ApiClient.instance.toggleBookmark("Bearer $token", idDokumen)
                fetchArsip(token) // Refresh list
            } catch (e: Exception) {
                _error.value = "Gagal mengubah bookmark."
            }
        }
    }
}
