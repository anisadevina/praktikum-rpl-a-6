package com.example.studyscope.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyscope.model.Matkul
import com.example.studyscope.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MataKuliahViewModel : ViewModel() {

    private val _matkulList = MutableStateFlow<List<Matkul>>(emptyList())
    val matkulList: StateFlow<List<Matkul>> = _matkulList

    private val _username = MutableStateFlow("Memuat...")
    val username: StateFlow<String> = _username

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _currentPage = MutableStateFlow(1)
    private val _lastPage = MutableStateFlow(1)

    fun updateSearchQuery(query: String, token: String) {
        _searchQuery.value = query
        _currentPage.value = 1
        _matkulList.value = emptyList()
        fetchMatkul(query, 1, token)
    }

    fun fetchMatkul(query: String = "", page: Int = 1, token: String = "") {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = ApiClient.instance.getMatkul("Bearer $token", query)
                if (response.isSuccessful) {
                    val body = response.body()

                    _username.value = body?.matkulData?.user?.username ?: "Pengguna"

                    val newList: List<Matkul> = body?.matkulData?.semuaMatkul?.data ?: emptyList()
                    _matkulList.value = if (page == 1) newList else _matkulList.value + newList
                    _currentPage.value = body?.matkulData?.semuaMatkul?.current_page ?: 1
                    _lastPage.value = body?.matkulData?.semuaMatkul?.last_page ?: 1
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
}