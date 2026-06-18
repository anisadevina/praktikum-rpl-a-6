<?php

namespace App\Http\Controllers\Concerns;

use Illuminate\Support\Facades\DB;

/**
 * Trait ini berisi query yang digunakan bersama oleh
 * BerandaController dan MatkulController agar tidak ada duplikasi.
 */
trait AmbilDataMatkul
{
    /**
     * Ambil mata kuliah yang terakhir diakses oleh user,
     * sekaligus menyertakan jumlah arsip yang sudah disetujui.
     *
     * @param  int  $idUser
     * @param  int  $limit
     * @return \Illuminate\Support\Collection
     */
    protected function ambilMatkulTerakhirDiakses(int $idUser, int $limit = 4)
    {
        return DB::table('riwayat_akses')
            ->join('mata_kuliah', 'riwayat_akses.id_matkul', '=', 'mata_kuliah.id_matkul')
            ->where('riwayat_akses.id_user', $idUser)
            ->select('mata_kuliah.*')
            ->orderBy('riwayat_akses.waktu_akses', 'desc')
            ->limit($limit)
            ->get()
            ->map(fn($matkul) => $this->sisipkanJumlahArsip($matkul));
    }

    /**
     * Sisipkan properti 'arsip' (jumlah dokumen disetujui) ke objek matkul.
     *
     * @param  object  $matkul
     * @return object
     */
    protected function sisipkanJumlahArsip(object $matkul): object
    {
        $matkul->arsip = DB::table('dokumen')
            ->where('id_matkul', $matkul->id_matkul)
            ->where('status', 'disetujui')
            ->count();

        return $matkul;
    }
}
