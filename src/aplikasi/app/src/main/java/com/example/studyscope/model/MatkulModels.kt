package com.example.studyscope.model
import com.google.gson.annotations.SerializedName

data class MatkulResponse(
    val status: String,
    @SerializedName("data")
    val matkulData: MatkulData
)

data class MatkulData(
    val user: UserData,
    val mataKuliahTerakhir: List<Matkul>,
    val semuaMatkul: SemuaMatkulPaginated,
    val query: String
)

data class SemuaMatkulPaginated(
    val data: List<Matkul>,
    val current_page: Int,
    val last_page: Int,
    val total: Int
)