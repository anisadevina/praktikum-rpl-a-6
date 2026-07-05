<?php

namespace Tests\Feature;

use Illuminate\Foundation\Testing\RefreshDatabase;
use Tests\TestCase;
use App\Models\User;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Hash;

class AuthTest extends TestCase
{
    use RefreshDatabase;

    /**
     * Test 1: Happy Case - Registrasi berhasil dengan data yang valid
     */
    public function test_registrasi_berhasil_jika_data_valid_dan_nim_terdaftar_di_master()
    {
        // 1. ARRANGE: Siapkan data master dan payload request
        // Karena ada validasi 'exists:master_mahasiswa_fatisda,nim', kita harus insert ke master dulu
        DB::table('master_mahasiswa_fatisda')->insert([
            'nim' => 'L0124021',
            'email_institusi' => 'ekaputrilee@student.uns.ac.id',
            'nama' => 'Lee Eka Putri',
            'prodi' => 'Informatika',
            'tahun_angkatan' => 2024

        ]);

        $payload = [
            'nim' => 'L0124021',
            'username' => 'ekaa',
            'email_user' => 'ekaputrilee@student.uns.ac.id',
            'password' => 'password123',
        ];

        // 2. ACT: Tembak endpoint register menggunakan method POST
        $response = $this->postJson('/register', $payload);

        // 3. ASSERT: Pastikan status HTTP 201 Created dan data masuk ke tabel users
        $response->assertStatus(201)
            ->assertJson([
                'status' => 'success',
                'message' => 'Registrasi berhasil, silakan login.',
            ]);

        $this->assertDatabaseHas('users', [
            'nim' => 'L0124021',
            'username' => 'ekaa',
            'email_user' => 'ekaputrilee@student.uns.ac.id'
        ]);
    }

    /**
     * Test 2: Edge Case / Unhappy Path - Registrasi gagal karena email bukan SSO UNS
     */
    public function test_registrasi_gagal_jika_format_email_bukan_sso_uns()
    {
        // 1. ARRANGE: Siapkan payload dengan email sembarangan (tidak pakai @student.uns.ac.id)
        $payload = [
            'nim' => 'L0124012',
            'username' => 'faadhilah',
            'email_user' => 'faadhilah@gmail.com', // Format email salah
            'password' => 'password123'
        ];

        // 2. ACT: Eksekusi endpoint register
        $response = $this->postJson('/register', $payload);

        // 3. ASSERT: Pastikan ditolak dengan HTTP 422 (Unprocessable Entity) karena gagal validasi regex
        $response->assertStatus(422)
            ->assertJsonValidationErrors(['email_user']);
    }

    /**
     * Test 3: Happy Case - Login berhasil dengan kredensial yang tepat
     */
    public function test_login_berhasil_dan_session_terbuat_jika_kredensial_benar()
    {
        // 1. ARRANGE: Buat satu user asli di database (password wajib di-hash)
        $user = User::factory()->create([
            'username' => 'studyscope_user',
            'password' => Hash::make('rahasia123'),
        ]);

        $payload = [
            'username' => 'studyscope_user',
            'password' => 'rahasia123',
        ];

        // 2. ACT: Tembak endpoint login
        $response = $this->postJson('/login', $payload);

        // 3. ASSERT: Pastikan HTTP 200, user terautentikasi (Auth::check), dan return JSON sukses
        $response->assertStatus(200)
            ->assertJson([
                'status' => 'success',
                'message' => 'Login berhasil',
            ]);

        $this->assertAuthenticatedAs($user); // Verifikasi session Laravel menyimpan auth user ini
    }

    /**
     * Test 4: Unhappy Path - Login gagal jika password salah
     */
    public function test_login_gagal_jika_kombinasi_password_salah()
    {
        // 1. ARRANGE: Buat user asli, namun siapkan payload dengan password salah
        User::factory()->create([
            'username' => 'studyscope_user',
            'password' => Hash::make('rahasia123'),
        ]);

        $payload = [
            'username' => 'studyscope_user',
            'password' => 'password_salah_ketik',
        ];

        // 2. ACT: Tembak endpoint login
        $response = $this->postJson('/login', $payload);

        // 3. ASSERT: Pastikan ditolak dengan HTTP 401 dan user tidak terautentikasi
        $response->assertStatus(401)
            ->assertJson([
                'message' => 'Username atau password salah',
            ]);

        $this->assertGuest(); // Verifikasi bahwa statusnya masih 'Guest' (tidak login)
    }
}