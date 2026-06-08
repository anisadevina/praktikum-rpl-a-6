<?php

namespace App\Http\Controllers;

use Illuminate\Support\Facades\DB;

class BerandaController extends Controller
{
    public function index()
    {
        return view('beranda');
    }
    public function getData()
    {
        $user = auth()->user();

        $mataKuliahTerakhir = DB::table('riwayat_akses')
            ->join('mata_kuliah', 'riwayat_akses.id_matkul', '=', 'mata_kuliah.id_matkul')
            ->where('riwayat_akses.id_user', $user->id_user)
            ->select('mata_kuliah.*')
            ->orderBy('riwayat_akses.waktu_akses', 'desc')
            ->limit(4)
            ->get()
            ->map(function ($item) {
                $item->arsip = DB::table('dokumen')
                    ->where('id_matkul', $item->id_matkul)
                    ->where('status', 'disetujui')
                    ->count();
                return $item;
            });

        $forumTerbaru = DB::table('forum_topik')
            ->join('users', 'forum_topik.id_user', '=', 'users.id_user')
            ->select('forum_topik.*', 'users.username')
            ->orderBy('forum_topik.waktu_topik', 'desc')
            ->limit(4)
            ->get();

        return response()->json([
            'status' => 'success',
            'data' => [
                'user' => $user,
                'mataKuliahTerakhir' => $mataKuliahTerakhir,
                'forumTerbaru' => $forumTerbaru
            ]
        ]);
    }
}