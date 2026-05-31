<?php

namespace App\Http\Controllers;

use App\Models\User;
use App\Http\Requests\RegisterRequest;
use App\Http\Requests\LoginRequest;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Auth;

class AuthController extends Controller
{
    public function register(RegisterRequest $request)
    {
        $user = User::create([
            'nim'        => $request->nim,
            'username'   => $request->username,
            'email_user' => $request->email_user,
            'password'   => Hash::make($request->password),
            'role'       => 'user',
        ]);

        return response()->json([
            'status'  => 'success',
            'message' => 'Registrasi berhasil, silakan login.',
            'user'    => $user
        ], 201);
    }

    public function login(LoginRequest $request)
    {
        $credentials = $request->only('username', 'password');

        if (Auth::attempt($credentials)) {
            $request->session()->regenerate();

            return response()->json([
                'status'   => 'success',
                'message'  => 'Login berhasil',
                'redirect' => '/beranda'
            ], 200);
        }

        return response()->json([
            'message' => 'Username atau password salah'
        ], 401);
    }

    public function logout(Request $request)
    {
        Auth::logout();

        $request->session()->invalidate();
        $request->session()->regenerateToken();

        return response()->json([
            'status'  => 'success',
            'message' => 'Logout berhasil'
        ]);
    }
}