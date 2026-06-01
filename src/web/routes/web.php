<?php

use Illuminate\Support\Facades\Route;
use Illuminate\Support\Facades\DB;
use App\Http\Controllers\AuthController;
use App\Http\Controllers\BerandaController;
use App\Http\Controllers\MatkulController;
use App\Http\Controllers\ForumController;
use App\Http\Controllers\UnggahController;

// Autensifiaksi 
Route::get('/', function () {
    return view('login');
})->name('login');

Route::get('/register', function () {
    return view('register');
})->name('register');

Route::post('/login',    [AuthController::class, 'login'])->name('login.proses');
Route::post('/register', [AuthController::class, 'register'])->name('register.proses');
Route::post('/logout',   [AuthController::class, 'logout'])->name('logout');

// Rute dari auth
Route::middleware('auth')->group(function () {

    // Beranda
    Route::get('/beranda',      [BerandaController::class, 'index'])->name('beranda');
    Route::get('/beranda/data', [BerandaController::class, 'getData'])->name('beranda.data');

    // Matkul
    Route::get('/matkul',             [MatkulController::class, 'index'])->name('matkul');
    Route::get('/matkul/data',        [MatkulController::class, 'getIndexData'])->name('matkul.data');
    Route::get('/matkul/detail',      [MatkulController::class, 'detail'])->name('matkul.detail');
    Route::get('/matkul/detail/data', [MatkulController::class, 'getDetailData'])->name('matkul.detail.data');

    // Forum
    Route::get('/forum',          [ForumController::class, 'index'])->name('forum');
    Route::get('/forum/data',     [ForumController::class, 'getData'])->name('forum.data');
    Route::post('/forum/topik',   [ForumController::class, 'buatTopik'])->name('forum.topik');
    Route::post('/forum/balasan', [ForumController::class, 'buatBalasan'])->name('forum.balasan');

    // ── Unggah (feature/unggah-umum) ────────────────────────
    Route::get('/unggah',       [UnggahController::class, 'index'])->name('unggah');
    Route::get('/unggah/form',  [UnggahController::class, 'create'])->name('unggah.detail');
    Route::get('/unggah/data',  [UnggahController::class, 'getData'])->name('unggah.data');
    Route::post('/unggah',      [UnggahController::class, 'upload'])->name('unggah.proses');

    // ── Review Dokumen (feature/review-dokumen) — dummy sementara ───
    Route::get('/review-dokumen', function () {
        return view('reviewList', ['user' => auth()->user()]);
    })->name('review-dokumen');

    Route::get('/review-dokumen/{id}', function ($id) {
        return view('reviewDetail', [
            'user'    => auth()->user(),
            'dokumen' => DB::table('dokumen')->where('id_dokumen', $id)->first(),
        ]);
    })->name('review-dokumen.detail');

    Route::post('/review-dokumen/keputusan/{id}', function ($id) {
                return redirect()->route('review-dokumen');
    })->name('review-dokumen.submit');

    // ── Arsip (feature/arsip) — dummy sementara 
    Route::get('/arsip', function () {
        return view('arsip', [
            'user'          => auth()->user(),
            'daftarArsip'   => collect([]),
            'bookmarkedIds' => [],
        ]);
    })->name('arsip');

    Route::post('/arsip/bookmark/{id}', function ($id) {
        // TODO: sambungkan ke ArsipController@toggleBookmark
        return response()->json(['bookmarked' => true]);
    })->name('arsip.bookmark');

});