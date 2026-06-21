<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\AuthController as ApiAuthController;
use App\Http\Controllers\BerandaController;
use App\Http\Controllers\MatkulController;
use App\Http\Controllers\ArsipController;

Route::post('/register', [ApiAuthController::class, 'register']);
Route::post('/login', [ApiAuthController::class, 'login']);

Route::middleware('auth:sanctum')->group(function () {
    Route::post('/logout', [ApiAuthController::class, 'logout']);

    Route::get('/beranda/data', [BerandaController::class, 'getData']);

    Route::get('/matkul/data', [MatkulController::class, 'getIndexData']);
    Route::get('/matkul/detail/data', [MatkulController::class, 'getDetailData']);

    Route::get('/arsip/data', [ArsipController::class, 'getData']);
    Route::post('/arsip/bookmark/{id}', [ArsipController::class, 'toggleBookmark']);

    Route::get('/arsip/view/{kode}', [ArsipController::class, 'viewDokumen']);


});
