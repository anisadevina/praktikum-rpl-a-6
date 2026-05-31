<?php

namespace App\Http\Requests;

use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Support\Facades\DB;

class RegisterRequest extends FormRequest
{
    public function authorize(): bool
    {
        return true;
    }

    public function rules(): array
    {
        return [
            'nim' => 'required|exists:master_mahasiswa_fatisda,nim|unique:users,nim',
            'username' => 'required|unique:users,username',
            'email_user' => [
                'required',
                'email',
                'unique:users,email_user',
                'regex:/^[\w\.-]+@student\.uns\.ac\.id$/i',
                function ($attribute, $value, $fail) {
                    $master = DB::table('master_mahasiswa_fatisda')
                        ->where('nim', $this->nim)
                        ->first();

                    if ($master && $master->email_institusi !== $value) {
                        $fail('Email tidak sesuai dengan NIM yang terdaftar.');
                    }
                },
            ],
            'password' => 'required|min:8',
        ];
    }

    public function messages(): array
    {
        return [
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
        ];
    }
}