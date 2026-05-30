<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\AuthController;
use App\Http\Controllers\BerandaController;
use App\Http\Controllers\MatkulController;

Route::get('/', function () {
    return view('login');
})->name('login');

Route::get('/register', function () {
    return view('register');
})->name('register');



Route::post('/login', [AuthController::class, 'login'])->name('login.proses');
Route::post('/register', [AuthController::class, 'register'])->name('register.proses');
Route::post('/logout', [AuthController::class, 'logout'])->name('logout');


// Kelompokkan rute yang butuh middleware 'auth'
Route::middleware('auth')->group(function () {
    Route::get('/beranda', [BerandaController::class, 'index'])->name('beranda');
    Route::get('/beranda/data', [BerandaController::class, 'getData'])->name('beranda.data');

    Route::get('/matkul', [MatkulController::class, 'index'])->name('matkul');
    Route::get('/matkul/data', [MatkulController::class, 'getIndexData'])->name('matkul.data');
    Route::get('/matkul/detail', [MatkulController::class, 'detail'])->name('matkul.detail');
    Route::get('/matkul/detail/data', [MatkulController::class, 'getDetailData'])->name('matkul.detail.data');

    Route::get('/arsip/view/{kode}', [MatkulController::class, 'viewArsip'])->name('arsip.view');

    Route::get('/arsip/dokumen/{id}', function ($id) {
        return view('lihat-dokumen', [
            'dokumen' => [
                'nama' => 'Soal UAS Organisasi Sistem Komputer',
                'tahun' => '2024',
                'tanggal' => '23 Mei 2026',
                'file_url' => '',
            ],
        ]);
    })->name('arsip.dokumen'); // dari jeje (dummy)

});
