package com.example.studyscope.model

data class DetailMatkulResponse(
    val status: String,
    val data: DetailMatkulData
)

data class DetailMatkulData(
    val user: UserData,
    val matkul: MatkulDetail,
    val teksKesulitan: String,
    val jumlahArsip: Int,
    val daftarArsip: List<Dokumen>
)

data class MatkulDetail(
    val id_matkul: Int,
    val nama_matkul: String,
    val deskripsi: String?,
    val tingkat_kesulitan: Double
)

data class Dokumen(
    val id_dokumen: Int,
    val judul: String,
    val kategori_file: String,
    val tahun_dokumen: Int,
    val waktu_unggah: String,
    val is_bookmarked: Boolean
)