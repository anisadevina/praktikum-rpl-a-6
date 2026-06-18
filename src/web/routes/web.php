<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\AuthController;
use App\Http\Controllers\BerandaController;
use App\Http\Controllers\MatkulController;
use App\Http\Controllers\ForumController;
use App\Http\Controllers\ArsipController;
use App\Http\Controllers\UnggahController;
use App\Http\Middleware\CekAdmin;
use App\Http\Controllers\ReviewDokumenController;


// Autensifiaksi 
Route::get('/', function () {
    return view('login');
})->name('login');

Route::get('/register', function () {
    return view('register');
})->name('register');

Route::post('/login', [AuthController::class, 'login'])->name('login.proses');
Route::post('/register', [AuthController::class, 'register'])->name('register.proses');
Route::post('/logout', [AuthController::class, 'logout'])->name('logout');

// Rute dari auth
Route::middleware('auth')->group(function () {

    // Beranda
    Route::get('/beranda', [BerandaController::class, 'index'])->name('beranda');
    Route::get('/beranda/data', [BerandaController::class, 'getData'])->name('beranda.data');

    // Matkul
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

    // Unggah
    Route::get('/unggah', [UnggahController::class, 'index'])->name('unggah');
    Route::get('/unggah/detail', [UnggahController::class, 'create'])->name('unggah.detail');
    Route::get('/unggah/data', [UnggahController::class, 'getData'])->name('unggah.data');
    Route::post('/unggah', [UnggahController::class, 'upload'])->name('unggah.proses');

    // admin 
    Route::middleware(CekAdmin::class)->group(function () {
        Route::get('/review-dokumen', [ReviewDokumenController::class, 'index'])->name('review-dokumen');
        Route::get('/review-dokumen/{id}', [ReviewDokumenController::class, 'detail'])->name('review-dokumen.detail');

        Route::get('/api/review-dokumen', [ReviewDokumenController::class, 'getListData']);
        Route::get('/api/review-dokumen/{id}', [ReviewDokumenController::class, 'getDetailData']);
        Route::post('/api/review-dokumen/{id}', [ReviewDokumenController::class, 'submitReview']);
    });

});