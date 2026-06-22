package com.example.studyscope.model

import com.google.gson.annotations.SerializedName

data class ArsipItem(
    @SerializedName("id_dokumen")           val idDokumen: Int,
    @SerializedName("judul")                val judul: String,
    @SerializedName("kategori_file")        val kategoriFile: String,
    @SerializedName("tahun_dokumen")        val tahunDokumen: Int,
    @SerializedName("waktu_unggah")         val waktuUnggah: String,
    @SerializedName("file_path")            val filePath: String,
    @SerializedName("nama_matkul")          val namaMatkul: String,
    @SerializedName("nama_dosen")           val namaDosen: String,
    @SerializedName("waktuUnggahFormatted") val waktuUnggahFormatted: String,
    @SerializedName("file_url")             val fileUrl: String,
)

data class ArsipData(
    @SerializedName("daftarArsip") val daftarArsip: List<ArsipItem>
)

data class ArsipResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data")   val data: ArsipData
)

data class BookmarkResponse(
    @SerializedName("status")     val status: String,
    @SerializedName("bookmarked") val bookmarked: Boolean,
    @SerializedName("message")    val message: String
)
