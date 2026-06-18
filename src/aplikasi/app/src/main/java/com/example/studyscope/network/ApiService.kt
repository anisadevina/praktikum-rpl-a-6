package com.example.studyscope.network

import com.example.studyscope.model.AuthResponse
import com.example.studyscope.model.BerandaResponse
import com.example.studyscope.model.DetailMatkulResponse
import com.example.studyscope.model.LoginRequest
import com.example.studyscope.model.MatkulResponse
import com.example.studyscope.model.RegisterRequest
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    // Menembak route POST /login
    @Headers("Accept: application/json")
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    // Menembak route POST /register
    @Headers("Accept: application/json")
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @Headers("Accept: application/json")
    @POST("logout")
    suspend fun logout(@Header("Authorization") token: String): Response<AuthResponse>

    // Menembak route GET /beranda/data
    @Headers("Accept: application/json")
    @GET("beranda/data")
    suspend fun getBeranda(
        @Header("Authorization") token: String
    ): Response<BerandaResponse>

    // Menembak route GET /matkul/data
    @Headers("Accept: application/json")
    @GET("matkul/data")
    suspend fun getMatkul(
        @Header("Authorization") token: String,
        @Query("q") query: String = "",
        @Query("page") page: Int = 1
    ): Response<MatkulResponse>

    // Menembak route GET /matkul/detail/data?id={id_matkul}
    @Headers("Accept: application/json")
    @GET("matkul/detail/data")
    suspend fun getDetailMatkul(
        @Header("Authorization") token: String,
        @Query("id") idMatkul: Int
    ): Response<DetailMatkulResponse>
}

object ApiClient {
    private const val BASE_URL = "http://10.219.113.103:8000/api/"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}