<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
<<<<<<< Updated upstream
use App\Models\User;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;
=======
use App\Models\user;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Auth;
>>>>>>> Stashed changes

class authController extends Controller
{
    public function register(Request $request)
    {
        $request->validate([
            'nim' => 'required|exists:master_mahasiswa_fatisda,nim|unique:users,nim',
            'username' => 'required|unique:users,username',
<<<<<<< Updated upstream
            'email_user' => [
                'required',
                'email',
                'unique:users,email_user',
                'regex:/^[\w\.-]+@student\.uns\.ac\.id$/i',
                function ($attribute, $value, $fail) use ($request) {
                    $master = DB::table('master_mahasiswa_fatisda')
                        ->where('nim', $request->nim)
                        ->first();

                    if ($master && $master->email_institusi !== $value) {
                        $fail('Email tidak sesuai dengan NIM yang terdaftar.');
                    }
                },
            ],
=======
            'email_user' => 'required|email|unique:users,email_user',
>>>>>>> Stashed changes
            'password' => 'required|min:8',
        ], [
            'nim.required' => 'NIM wajib diisi.',
            'nim.exists' => 'NIM tidak ditemukan dalam data mahasiswa.',
<<<<<<< Updated upstream
            'nim.unique' => 'NIM sudah terdaftar, silakan login.',
            'username.required' => 'Username wajib diisi.',
            'username.unique' => 'Username sudah terdaftar, silakan pilih yang lain.',
            'email_user.required' => 'Email wajib diisi.',
            'email_user.email' => 'Format email tidak valid.',
            'email_user.regex' => 'Harus menggunakan email SSO UNS',
            'email_user.unique' => 'Email sudah terdaftar, silakan login.',
            'password.required' => 'Password wajib diisi.',
            'password.min' => 'Password minimal 8 karakter.',
        ]);

        User::create([
=======
            'nim.unique' => 'NIM sudah terdaftar.',
        ]);

        user::create([
>>>>>>> Stashed changes
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
