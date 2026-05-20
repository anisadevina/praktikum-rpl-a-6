<?php

use App\Http\Controllers\authController;
use Illuminate\Support\Facades\Route;
use Illuminate\Support\Facades\DB;

Route::get('/', function () {
    return view('login');
})->name('login');

Route::get('/register', function () {
    return view('register');
})->name('register');

Route::post('/login', [authController::class, 'login'])->name('login.proses');
Route::post('/register', [authController::class, 'register'])->name('register.proses');
Route::post('/logout', [authController::class, 'logout'])->name('logout');

Route::get('/beranda', function () {
    // return view('beranda', ['user' => auth()->user()]);
    $user = auth()->user();

    // ambil 4 mata kuliah terakhir yang diakses oleh user
    $mataKuliahTerakhir = DB::table('riwayat_akses')
        ->join('mata_kuliah', 'riwayat_akses.id_matkul', '=', 'mata_kuliah.id_matkul')
        ->where('riwayat_akses.id_user', $user->id_user)
        ->select('mata_kuliah.*')
        ->orderBy('riwayat_akses.waktu_akses', 'desc')
        ->limit(4)
        ->get()
        ->map(function ($item) {
            $item->arsip = DB::table('dokumen')->where('id_matkul', $item->id_matkul)->count();
            return $item;
        });

    $forumTerbaru = DB::table('forum_topik')
        ->join('users', 'forum_topik.id_user', '=', 'users.id_user')
        ->select('forum_topik.*', 'users.username')
        ->orderBy('forum_topik.waktu_topik', 'desc')
        ->limit(4)
        ->get();

    return view('beranda', [
        'user' => $user,
        'mataKuliahTerakhir' => $mataKuliahTerakhir,
        'forumTerbaru' => $forumTerbaru,
    ]);
})->name('beranda')->middleware('auth');

Route::get('/search', [authController::class, 'search'])->name('search')->middleware('auth');
Route::get('/matkul', [authController::class, 'matkul'])->name('matkul')->middleware('auth');
