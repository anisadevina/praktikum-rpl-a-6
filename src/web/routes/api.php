<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\MobileController;
use App\Http\Controllers\Api\AuthController; // Buat controller login terpisah

// Rute untuk Login (Mendapatkan Token)
Route::post('/login', [AuthController::class, 'login']);

// Rute yang butuh Token (Harus Login Dulu)
Route::middleware('auth:sanctum')->group(function () {
    Route::get('/beranda', [MobileController::class, 'getBeranda']);
    // Tambahkan rute untuk detail matkul di sini nanti
});
