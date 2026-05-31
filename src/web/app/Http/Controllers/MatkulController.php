<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Carbon\Carbon;
use Illuminate\Support\Facades\Crypt;

class MatkulController extends Controller
{
    public function index()
    {
        return view('matkul');
    }

    public function getIndexData(Request $request)
    {
        $user = auth()->user();
        $query = $request->input('q', '');

        // 1. Ambil matkul terakhir dilihat (limit 4)
        $mataKuliahTerakhir = DB::table('riwayat_akses')
            ->join('mata_kuliah', 'riwayat_akses.id_matkul', '=', 'mata_kuliah.id_matkul')
            ->where('riwayat_akses.id_user', $user->id_user)
            ->select('mata_kuliah.*')
            ->orderBy('riwayat_akses.waktu_akses', 'desc')
            ->limit(4)
            ->get()
            ->map(function ($item) {
                $item->arsip = DB::table('dokumen')->where('id_matkul', $item->id_matkul)->count();
                return $item;
            });

        // 2. Ambil semua matkul dengan pagination
        $semuaMatkul = DB::table('mata_kuliah')
            ->when($query, function ($q) use ($query) {
                $q->where('nama_matkul', 'like', '%' . $query . '%');
            })
            ->paginate(12);

        // 3. Sisipkan jumlah arsip ke data yang sudah di-paginate
        $semuaMatkul->getCollection()->transform(function ($item) {
            $item->arsip = DB::table('dokumen')->where('id_matkul', $item->id_matkul)->count();
            return $item;
        });

        return response()->json([
            'status' => 'success',
            'data' => [
                'user' => $user,
                'mataKuliahTerakhir' => $mataKuliahTerakhir,
                'semuaMatkul' => $semuaMatkul,
                'query' => $query,
            ]
        ]);
    }

    public function detail()
    {
        return view('detailMatkul');
    }

    public function getDetailData(Request $request)
    {
        $id_matkul = $request->query('id');
        $user = auth()->user();

        if (!$id_matkul) {
            return response()->json([
                'status' => 'error',
                'message' => 'ID tidak ditemukan'
            ], 400);
        }

        $this->catatRiwayatAkses($user->id_user, $id_matkul);

        $matkul = DB::table('mata_kuliah')->where('id_matkul', $id_matkul)->first();

        if (!$matkul) {
            return response()->json([
                'status' => 'error',
                'message' => 'Mata kuliah tidak ditemukan'
            ], 404);
        }

        $teksKesulitan = $this->konversiTingkatKesulitan($matkul->tingkat_kesulitan);

        $tahunFilter = $request->query('tahun');
        $queryArsip = DB::table('dokumen')->where('id_matkul', $id_matkul);

        if (!empty($tahunFilter)) {
            $queryArsip->where('tahun_dokumen', $tahunFilter);
        }

        $jumlahArsip = $queryArsip->count();
        $daftarArsip = $queryArsip->get()->map(function ($item) {
            $item->kodeRahasia = Crypt::encryptString($item->id_dokumen ?? $item->id);
            return $item;
        });

        return response()->json([
            'status' => 'success',
            'data' => [
                'user' => $user,
                'matkul' => $matkul,
                'teksKesulitan' => $teksKesulitan,
                'jumlahArsip' => $jumlahArsip,
                'daftarArsip' => $daftarArsip,
            ]
        ]);
    }

    // --- Private Methods ---

    private function catatRiwayatAkses($id_user, $id_matkul)
    {
        DB::table('riwayat_akses')->updateOrInsert(
            ['id_user' => $id_user, 'id_matkul' => $id_matkul],
            ['waktu_akses' => Carbon::now()]
        );
    }

    private function konversiTingkatKesulitan($skor)
    {
        if ($skor >= 1.00 && $skor < 2.00)
            return 'Materi cenderung sulit sekali';
        if ($skor >= 2.00 && $skor < 3.00)
            return 'Materi cenderung sulit';
        if ($skor >= 3.00 && $skor < 4.00)
            return 'Materi cenderung sedang';
        if ($skor >= 4.00 && $skor < 5.00)
            return 'Materi cenderung mudah';
        return 'Materi cenderung mudah sekali';
    }

    public function viewArsip($kode)
    {
        try {
            $id_dokumen = Crypt::decryptString($kode);
        } catch (\Illuminate\Contracts\Encryption\DecryptException $e) {
            abort(404, 'tautan dokumen tidak valid atau sudah kadaluarsa.');
        }
        // Cari dokumen berdasarkan ID
        $dokumen = DB::table('dokumen')->where('id_dokumen', $id_dokumen)->first();

        if (!$dokumen || !$dokumen->file_path) {
            abort(404, 'Arsip tidak ditemukan.');
        }

        // Ambil lokasi file fisik di storage
        $path = storage_path('app/public/' . $dokumen->file_path);

        if (!file_exists($path)) {
            abort(404, 'File fisik tidak tersedia di server.');
        }

        // Tampilkan file langsung di browser (PDF viewer bawaan)
        return response()->file($path);
    }
}