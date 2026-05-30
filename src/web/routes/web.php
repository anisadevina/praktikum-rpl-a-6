<?php

use App\Http\Controllers\AuthController;
use App\Http\Controllers\BerandaController;
use App\Http\Controllers\MatkulController;
use App\Http\Controllers\ForumController;
use Illuminate\Support\Facades\Route;
use Illuminate\Support\Facades\DB;

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

// -- BERANDA --
Route::middleware('auth')->group(function () {
    Route::get('/beranda', [BerandaController::class, 'index'])->name('beranda');
    Route::get('/search', [MatkulController::class, 'search'])->name('search');
    Route::get('/matkul', [MatkulController::class, 'index'])->name('matkul');
    Route::get('/matkul/detail', [MatkulController::class, 'detail'])->name('matkul.detail');
});

// -- FORUM --
Route::get('/forum', [ForumController::class, 'forum'])->name('forum')->middleware('auth');
Route::post('/forum/topik', [ForumController::class, 'buatTopik'])->name('forum.topik')->middleware('auth');
Route::post('/forum/balasan', [ForumController::class, 'buatBalasan'])->name('forum.balasan')->middleware('auth');

// -- UNGGAH --
Route::get('/unggah', function () {
    $user = auth()->user();
    $mataKuliah = DB::table('mata_kuliah')->orderBy('nama_matkul')->get();
    $dosen = DB::table('dosen')->orderBy('nama_dosen')->get();
    return view('unggah', [
        'user' => $user,
        'mataKuliah' => $mataKuliah,
        'dosen' => $dosen,
    ]);
})->middleware('auth')->name('unggah');