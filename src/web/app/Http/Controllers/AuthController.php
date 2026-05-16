<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\user;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Auth;

class authController extends Controller
{
    public function register(Request $request)
    {
        $request->validate([
            'nim' => 'required|exists:master_mahasiswa_fatisda,nim|unique:users,nim',
            'username' => 'required|unique:users,username',
            'email_user' => 'required|email|unique:users,email_user',
            'password' => 'required|min:8',
        ], [
            'nim.required' => 'NIM wajib diisi.',
            'nim.exists' => 'NIM tidak ditemukan dalam data mahasiswa.',
            'nim.unique' => 'NIM sudah terdaftar.',
        ]);

        user::create([
            'nim' => $request->nim,
            'username' => $request->username,
            'email_user' => $request->email_user,
            'password' => Hash::make($request->password),
            'role' => 'user',
        ]);

        return redirect()->route('login')->with('success', 'Registrasi berhasil. Silakan login.');
    }

    public function login(Request $request)
    {

    }

    public function logout(Request $request)
    {
        Auth::logout();
        $request->session()->invalidate();
        $request->session()->regenerateToken();
        return redirect('/login')->with('success', 'Kamu telah berhasil logout.');

    }
}
