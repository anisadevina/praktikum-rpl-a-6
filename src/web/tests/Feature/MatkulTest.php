<?php

namespace Tests\Feature;

use Illuminate\Foundation\Testing\RefreshDatabase;
use Tests\TestCase;
use App\Models\User;
use Illuminate\Support\Facades\DB;

class MatkulTest extends TestCase
{
    use RefreshDatabase;

    /**
     * Test 1: Happy Case - Endpoint daftar matkul mengembalikan data dengan benar
     */
    public function test_user_dapat_melihat_daftar_mata_kuliah_di_halaman_utama_matkul()
    {
        // 1. ARRANGE: Siapkan user dan 3 data mata kuliah di database
        $user = User::factory()->create();

        DB::table('mata_kuliah')->insert([
            ['id_matkul' => 1, 'nama_matkul' => 'Kalkulus 1', 'tingkat_kesulitan' => 4.5],
            ['id_matkul' => 2, 'nama_matkul' => 'Fisika Dasar', 'tingkat_kesulitan' => 3.0],
            ['id_matkul' => 3, 'nama_matkul' => 'Logika Informatika', 'tingkat_kesulitan' => 2.5]
        ]);

        // 2. ACT: Akses endpoint daftar matkul
        $response = $this->actingAs($user)->getJson('/matkul/data');

        // 3. ASSERT: Verifikasi status dan struktur paginasi yang dikembalikan
        $response->assertStatus(200)
            ->assertJsonStructure([
                'status',
                'data' => [
                    'semuaMatkul' => [
                        'data' => [
                            '*' => ['id_matkul', 'nama_matkul']
                        ],
                        'current_page',
                        'last_page'
                    ]
                ]
            ]);
    }

    /**
     * Test 2: Happy Case - Fitur pencarian mata kuliah berfungsi dengan tepat
     */
    public function test_fitur_pencarian_matkul_hanya_mengembalikan_matkul_yang_relevan()
    {
        // 1. ARRANGE: Buat user dan data mata kuliah
        $user = User::factory()->create();
        DB::table('mata_kuliah')->insert([
            ['id_matkul' => 1, 'nama_matkul' => 'Metode Numerik', 'tingkat_kesulitan' => 4.0],
            ['id_matkul' => 2, 'nama_matkul' => 'Aljabar Linear', 'tingkat_kesulitan' => 3.5],
        ]);

        $kataKunci = 'Numerik';

        // 2. ACT: Tembak endpoint dengan parameter pencarian (q)
        $response = $this->actingAs($user)->getJson("/matkul/data?q={$kataKunci}");

        // 3. ASSERT: Pastikan Metode Numerik ada, dan Aljabar Linear tidak ada di hasil
        $response->assertStatus(200)
            ->assertJsonFragment(['nama_matkul' => 'Metode Numerik'])
            ->assertJsonMissing(['nama_matkul' => 'Aljabar Linear']);
    }

    /**
     * Test 3: Unhappy Path / Edge Case - Akses detail matkul yang ID-nya tidak ada
     */
    public function test_mengakses_detail_matkul_dengan_id_palsu_akan_mengembalikan_error_404()
    {
        // 1. ARRANGE: Buat user, tapi SENGAJA biarkan tabel mata_kuliah kosong
        $user = User::factory()->create();
        $idPalsu = 999;

        // 2. ACT: Akses endpoint detail matkul menggunakan ID yang tidak ada
        $response = $this->actingAs($user)->getJson("/matkul/detail/data?id={$idPalsu}");

        // 3. ASSERT: Harus ditolak dengan status 404 (Not Found)
        $response->assertStatus(404)
            ->assertJson([
                'status' => 'error',
                'message' => 'Mata kuliah tidak ditemukan'
            ]);
    }

    /**
     * Test 4: Happy Case - Detail matkul menampilkan arsip dokumen dengan tepat
     */
    public function test_halaman_detail_matkul_berhasil_menampilkan_data_dan_arsip_yang_disetujui()
    {
        // 1. ARRANGE: Siapkan data user, dosen, matkul, dan dokumen yang statusnya 'disetujui'
        $user = User::factory()->create();

        DB::table('mata_kuliah')->insert([
            'id_matkul' => 1,
            'nama_matkul' => 'Basis Data',
            'tingkat_kesulitan' => 3.5
        ]);

        DB::table('dosen')->insert([
            'id_dosen' => 1,
            'nama_dosen' => 'Dr. Budi',
            'nuptk' => '1234567890'
        ]);

        DB::table('dokumen')->insert([
            'id_dokumen' => 1,
            'id_user' => $user->id_user,
            'id_matkul' => 1,
            'id_dosen' => 1,
            'judul' => 'Materi ERD',
            'kategori_file' => 'materi',
            'tahun_dokumen' => 2024,
            'file_path' => 'docs/erd.pdf',
            'status' => 'disetujui' // Hanya arsip yang disetujui yang boleh tampil
        ]);

        // 2. ACT: Akses endpoint detail matkul yang valid
        $response = $this->actingAs($user)->getJson("/matkul/detail/data?id=1");

        // 3. ASSERT: Verifikasi kembalian datanya (jumlah arsip = 1)
        $response->assertStatus(200)
            ->assertJson([
                'status' => 'success',
                'data' => [
                    'matkul' => ['nama_matkul' => 'Basis Data'],
                    'jumlahArsip' => 1,
                ]
            ]);

        // Verifikasi bahwa riwayat akses sudah dicatat di database
        $this->assertDatabaseHas('riwayat_akses', [
            'id_user' => $user->id_user,
            'id_matkul' => 1
        ]);
    }
}