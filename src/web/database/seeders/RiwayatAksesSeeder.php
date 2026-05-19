<?php

namespace Database\Seeders;

use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class RiwayatAksesSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        // 1. Ambil ID dari mata kuliah yang sudah ada di database
        $matkuls = DB::table('Mata_Kuliah')->limit(4)->get();

        // 2. Pastikan data ditemukan agar tidak error saat loop
        if ($matkuls->isNotEmpty()) {
            foreach ($matkuls as $index => $matkul) {
                // 3. Langsung insert ke Riwayat_Akses menggunakan ID yang didapat
                DB::table('Riwayat_Akses')->insert([
                    'id_user' => 1, // Pastikan user dengan ID 1 sudah ada
                    'id_matkul' => $matkul->id_matkul, // Ambil ID dari hasil query di atas
                    'waktu_akses' => now()->subMinutes($index * 10), // Memberi selisih waktu
                ]);
            }
        }
    }
}