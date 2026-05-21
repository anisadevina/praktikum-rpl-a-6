<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class MobileController extends Controller
{
    // 1. Endpoint Beranda Khusus Mobile
    public function getBeranda(Request $request)
    {
        $user = $request->user(); // Ambil user dari token Sanctum

        // Ambil riwayat mata kuliah terakhir saja (Tanpa forum)
        $mataKuliahTerakhir = DB::table('Riwayat_Akses')
            ->join('Mata_Kuliah', 'Riwayat_Akses.id_matkul', '=', 'Mata_Kuliah.id_matkul')
            ->where('Riwayat_Akses.id_user', $user->id_user)
            ->select('Mata_Kuliah.*')
            ->orderBy('Riwayat_Akses.waktu_akses', 'desc')
            ->limit(4)
            ->get()
            ->map(function ($item) {
                // Konversi arsip ke angka
                $item->arsip = DB::table('Dokumen')->where('id_matkul', $item->id_matkul)->count();
                return $item;
            });

        // Kembalikan data dalam bentuk JSON
        return response()->json([
            'status' => 'success',
            'message' => 'Data beranda berhasil diambil',
            'data' => [
                'user' => [
                    'username' => $user->username,
                    'email' => $user->email_user
                ],
                'terakhir_dilihat' => $mataKuliahTerakhir
            ]
        ], 200);
    }
}
