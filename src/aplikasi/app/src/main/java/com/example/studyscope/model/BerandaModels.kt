package com.example.studyscope.model
import com.google.gson.annotations.SerializedName
data class UserData(
    val id_user: Int,
    val username: String,
    val role: String
)

data class Matkul(
    val id_matkul: Int,
    val nama_matkul: String,
    val tingkat_kesulitan: Double,
    val arsip: Int
)

data class BerandaData(
    val user: UserData,
    @SerializedName("mata_kuliah_terakhir")
    val mataKuliahTerakhir: List<Matkul>
)

data class BerandaResponse(
    val status: String,
    val data: BerandaData
)