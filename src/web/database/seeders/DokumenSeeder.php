<?php

namespace Database\Seeders;

use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;
use Carbon\Carbon;

class DokumenSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        DB::table('dokumen')->delete();

        $dokumenDummy = [
            [
                'id_dokumen' => 1,
                'id_user' => 1,
                'id_matkul' => 4,
                'id_dosen' => 1,
                'judul' => 'Tugas Kalkulus 1',
                'kategori_file' => 'tugas',
                'tahun_dokumen' => 2024,
                'file_path' => 'arsip/tugas-kalkulus-1.pdf',
                'status' => 'disetujui',
                'waktu_unggah' => Carbon::now()->subDays(2),
                'catatan_admin' => null,
            ]
        ];

        DB::table('dokumen')->insert($dokumenDummy);
    }
}
