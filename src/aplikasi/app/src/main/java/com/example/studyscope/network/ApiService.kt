package com.example.studyscope.network

import com.example.studyscope.model.AuthResponse
import com.example.studyscope.model.BerandaResponse
import com.example.studyscope.model.LoginRequest
import com.example.studyscope.model.RegisterRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface ApiService {
    // Menembak route POST /login
    @Headers("Accept: application/json")
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    // Menembak route POST /register
    @Headers("Accept: application/json")
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    // Menembak route GET /beranda/data
    @Headers("Accept: application/json")
    @GET("beranda/data")
    suspend fun getBeranda(
        @Header("Authorization") token: String
    ): Response<BerandaResponse>
}

object ApiClient {
    private const val BASE_URL = "http://192.168.1.7:8000/api/" // sesuaikan masing masing hp

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}