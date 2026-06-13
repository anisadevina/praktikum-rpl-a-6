package com.example.studyscope.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyscope.model.BerandaData
import com.example.studyscope.model.Matkul
import com.example.studyscope.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BerandaViewModel : ViewModel() {

    private val _berandaData = MutableStateFlow<BerandaData?>(null)
    val berandaData: StateFlow<BerandaData?> = _berandaData

    private val _filteredMatkul = MutableStateFlow<List<Matkul>>(emptyList())
    val filteredMatkul: StateFlow<List<Matkul>> = _filteredMatkul

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        val allMatkul = _berandaData.value?.mataKuliahTerakhir ?: emptyList()

        if (query.isBlank()) {
            _filteredMatkul.value = allMatkul
        } else {
            // Saring berdasarkan nama matkul yang diketik
            _filteredMatkul.value = allMatkul.filter {
                it.nama_matkul.contains(query, ignoreCase = true)
            }
        }
    }

    fun fetchBeranda(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = ApiClient.instance.getBeranda("Bearer $token")
                if (response.isSuccessful && response.body()?.status == "success") {
                    val data = response.body()?.data
                    _berandaData.value = data
                    _filteredMatkul.value = data?.mataKuliahTerakhir ?: emptyList() // Isi data awal
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

    fun logout(token: String, onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                ApiClient.instance.logout("Bearer $token")
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                onLogoutSuccess()
            }
        }
    }
}