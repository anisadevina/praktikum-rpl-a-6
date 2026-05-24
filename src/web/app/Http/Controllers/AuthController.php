<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\User;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;

class authController extends Controller
{
    public function register(Request $request)
    {
        $request->validate([
            'nim' => 'required|exists:master_mahasiswa_fatisda,nim|unique:users,nim',
            'username' => 'required|unique:users,username',
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
            'password' => 'required|min:8',
        ], [
            'nim.required' => 'NIM wajib diisi.',
            'nim.exists' => 'NIM tidak ditemukan dalam data mahasiswa.',
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
        $request->validate([
            'username' => 'required',
            'password' => 'required',
        ], [
            'username.required' => 'Username wajib diisi.',
            'password.required' => 'Password wajib diisi.',
        ]);

        $credentials = $request->only('username', 'password');

        if (Auth::attempt($credentials)) {
            $request->session()->regenerate();
            return redirect()->intended('/beranda');
        }

        return back()->withErrors([
            'password' => 'Username atau password salah.',
        ])->withInput($request->only('password'));
    }

}
