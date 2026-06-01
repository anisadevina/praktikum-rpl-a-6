<?php

use Illuminate\Support\Facades\Route;
use Illuminate\Support\Facades\DB;
use App\Http\Controllers\AuthController;
use App\Http\Controllers\BerandaController;
use App\Http\Controllers\MatkulController;
use App\Http\Controllers\ForumController;
use App\Http\Controllers\ArsipController;

// -- AUTENTIKASI --
Route::get('/', function () {
    return view('login');
})->name('login');

Route::get('/register', function () {
    return view('register');
})->name('register');

Route::post('/login', [AuthController::class, 'login'])->name('login.proses');
Route::post('/register', [AuthController::class, 'register'])->name('register.proses');
Route::post('/logout', [AuthController::class, 'logout'])->name('logout');

// -- RUTE YANG BUTUH LOGIN --
Route::middleware('auth')->group(function () {

    // Beranda
    Route::get('/beranda', [BerandaController::class, 'index'])->name('beranda');
    Route::get('/beranda/data', [BerandaController::class, 'getData'])->name('beranda.data');

    // Matkul
    Route::get('/search', [MatkulController::class, 'search'])->name('search');
    Route::get('/matkul', [MatkulController::class, 'index'])->name('matkul');
    Route::get('/matkul/data', [MatkulController::class, 'getIndexData'])->name('matkul.data');
    Route::get('/matkul/detail', [MatkulController::class, 'detail'])->name('matkul.detail');
    Route::get('/matkul/detail/data', [MatkulController::class, 'getDetailData'])->name('matkul.detail.data');

    // Forum
    Route::get('/forum', [ForumController::class, 'index'])->name('forum');
    Route::get('/forum/data', [ForumController::class, 'getData'])->name('forum.data');
    Route::post('/forum/topik', [ForumController::class, 'buatTopik'])->name('forum.topik');
    Route::post('/forum/balasan', [ForumController::class, 'buatBalasan'])->name('forum.balasan');

    // Arsip
    Route::get('/arsip', [ArsipController::class, 'index'])->name('arsip');
    Route::get('/arsip/data', [ArsipController::class, 'getData'])->name('arsip.data');
    Route::post('/arsip/bookmark/{id}', [ArsipController::class, 'toggleBookmark'])->name('arsip.bookmark');

    // Arsip View (dari MatkulController)
    Route::get('/arsip/view/{kode}', [MatkulController::class, 'viewArsip'])->name('arsip.view');
});