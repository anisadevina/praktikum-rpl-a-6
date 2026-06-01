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
    Route::get('/arsip', function () {
        return "Ini halaman Arsip sementara"; // Nanti tinggal arahkan ke Controller
    })->name('arsip');
    
    Route::get('/arsip/view/{kode}', [MatkulController::class, 'viewArsip'])->name('arsip.view');

    // Unggah
    Route::get('/unggah', [UnggahController::class, 'index'])->name('unggah');
    Route::get('/unggah/data', [UnggahController::class, 'getData'])->name('unggah.data');
    Route::post('/unggah', [UnggahController::class, 'upload'])->name('unggah.proses');

    // -- REVIEW DOKUMEN (ADMIN) --
    // 1. Halaman List Review
    Route::get('/review-dokumen', function () {
        return view('reviewList', [
            'user' => auth()->user()
        ]);
    })->name('review-dokumen');

    // 2. Halaman Detail Review
    Route::get('/review-dokumen/{id}', function ($id) {
        return view('reviewDetail', [
            'user' => auth()->user()
        ]);
    })->name('review-dokumen.detail');

    // 3. Tombol Submit (Kirim)
    Route::post('/review-dokumen/keputusan/{id}', function ($id) {
        return redirect()->route('review-dokumen');
    })->name('review-dokumen.submit');

    // -- LIHAT DOKUMEN --
    Route::get('/dokumen/{id}', function ($id) {
        $user = auth()->user();
        $dokumen = DB::table('dokumen')->where('id_dokumen', $id)->first();
        return view('lihat-dokumen', ['user' => $user, 'dokumen' => $dokumen]);
    })->name('lihat-dokumen');

});