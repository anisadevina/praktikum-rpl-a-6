<?php

use App\Http\Controllers\authController;
use Illuminate\Support\Facades\Route;

Route::get('/', function () {
    return view('login');
})->name('login');

Route::get('/register', function () {
    return view('register');
})->name('register');

Route::post('/login', [authController::class, 'login'])->name('login.proses');
Route::post('/register', [authController::class, 'register'])->name('register.proses');
Route::post('/logout', [authController::class, 'logout'])->name('logout');