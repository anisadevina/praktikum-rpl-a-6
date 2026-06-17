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
                    _detailData.value = response.body()?.data
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

    fun toggleBookmark(token: String, idDokumen: Int) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.toggleBookmark("Bearer $token", idDokumen)
                if (response.isSuccessful) {
                    // Update state lokal tanpa fetch ulang
                    _detailData.value = _detailData.value?.let { data ->
                        data.copy(
                            daftarArsip = data.daftarArsip.map { dokumen ->
                                if (dokumen.id_dokumen == idDokumen) {
                                    dokumen.copy(is_bookmarked = !dokumen.is_bookmarked)
                                } else dokumen
                            }
                        )
                    }
                }
            } catch (e: Exception) {
                // silent fail
            }
        }
    }
}