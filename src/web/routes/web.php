<?php

use Illuminate\Support\Facades\Route;
use Illuminate\Support\Facades\DB;
use App\Http\Controllers\AuthController;
use App\Http\Controllers\BerandaController;
use App\Http\Controllers\MatkulController;
use App\Http\Controllers\ForumController;
use App\Http\Controllers\UnggahController;

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
    Route::get('/arsip/view/{kode}', [MatkulController::class, 'viewArsip'])->name('arsip.view');

    // Unggah
    Route::get('/unggah', [UnggahController::class, 'index'])->name('unggah');
    Route::get('/unggah/data', [UnggahController::class, 'getData'])->name('unggah.data');
    Route::post('/unggah', [UnggahController::class, 'upload'])->name('unggah.proses');
});

// -- UNGGAH ADMIN --
Route::get('/unggah/admin', function () {
    $user = auth()->user();
    $mataKuliah = DB::table('mata_kuliah')->orderBy('nama_matkul')->get();
    $dosen = DB::table('dosen')->orderBy('nama_dosen')->get();
    return view('unggah-admin', ['user' => $user, 'mataKuliah' => $mataKuliah, 'dosen' => $dosen]);
})->middleware('auth')->name('unggah.admin');

Route::get('/unggah/review/{id}', function ($id) {
    $user = auth()->user();
    return view('unggah-review', ['user' => $user, 'dokumen' => null]);
})->middleware('auth')->name('unggah.review');

Route::post('/unggah/keputusan/{id}', function () {
    return redirect()->route('unggah.admin');
})->middleware('auth')->name('unggah.keputusan');

Route::get('/dokumen/{id}', function ($id) {
    $user = auth()->user();
    $dokumen = DB::table('dokumen')->where('id_dokumen', $id)->first();
    return view('lihat-dokumen', ['user' => $user, 'dokumen' => $dokumen]);
})->middleware('auth')->name('lihat-dokumen');