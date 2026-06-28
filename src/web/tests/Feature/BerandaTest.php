<?php

namespace Tests\Feature;

use Illuminate\Foundation\Testing\RefreshDatabase;
use Tests\TestCase;
use App\Models\User;
use Illuminate\Support\Facades\DB;

class BerandaTest extends TestCase
{
    use RefreshDatabase;

    /**
     * Test 1: Happy Case - User terautentikasi dapat melihat data beranda dengan lengkap
     */
    public function test_user_yang_sudah_login_dapat_melihat_data_beranda_dengan_format_yang_benar()
    {
        // 1. ARRANGE: Siapkan user dan data forum
        // Kita menggunakan UserFactory yang sebelumnya sudah kamu perbaiki (otomatis insert ke master)
        $user = User::factory()->create([
            'username' => 'mahasiswa_uns'
        ]);

        // Tambahkan 1 topik forum agar balasan array 'forumTerbaru' tidak kosong
        DB::table('forum_topik')->insert([
            'id_user' => $user->id_user,
            'tag' => 'general',
            'pesan_topik' => 'Halo semuanya, selamat pagi!',
            'is_anonim' => false,
            'waktu_topik' => now()
        ]);

        // 2. ACT: Simulasikan user login (actingAs) lalu akses endpoint data beranda
        $response = $this->actingAs($user)->getJson('/beranda/data');

        // 3. ASSERT: Verifikasi status 200 dan struktur JSON yang sesuai dengan BerandaController
        $response->assertStatus(200)
            ->assertJson([
                'status' => 'success',
                'data' => [
                    'user' => [
                        'username' => 'mahasiswa_uns'
                    ]
                ]
            ])
            ->assertJsonStructure([
                'status',
                'data' => [
                    'user',
                    'mataKuliahTerakhir',
                    'forumTerbaru' => [
                        '*' => ['id_topik', 'pesan_topik', 'username']
                    ]
                ]
            ]);
    }

    /**
     * Test 2: Unhappy Path - Akses ditolak jika pengunjung belum login
     */
    public function test_akses_data_beranda_ditolak_jika_user_belum_login()
    {
        // 1. ARRANGE: Tidak ada user yang di-set (mensimulasikan tamu/guest)

        // 2. ACT: Langsung mencoba menembak URL beranda
        $response = $this->getJson('/beranda/data');

        // 3. ASSERT: Harus mengembalikan error 401 (Unauthorized) karena diblokir middleware 'auth'
        $response->assertStatus(401);
    }

    /**
     * Test 3: Edge Case - Menangani kondisi jika database forum dan riwayat matkul masih kosong
     */
    public function test_beranda_tetap_aman_dan_mengembalikan_array_kosong_jika_belum_ada_aktivitas()
    {
        // 1. ARRANGE: Buat user baru, tetapi biarkan tabel forum dan matkul benar-benar kosong
        $user = User::factory()->create();

        // 2. ACT: Akses endpoint data beranda
        $response = $this->actingAs($user)->getJson('/beranda/data');

        // 3. ASSERT: Status harus 200 (sukses dimuat), tetapi array datanya kosong
        $response->assertStatus(200)
            ->assertJsonCount(0, 'data.forumTerbaru')
            ->assertJsonCount(0, 'data.mataKuliahTerakhir');
    }
}