<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Crypt;
use Carbon\Carbon;

class ArsipController extends Controller
{
    public function index(Request $request)
    {
        $user = auth()->user();
        $daftarArsip = $this->queryArsipBookmark($user->id_user, $request)
            ->get()
            ->map(fn($item) => $this->tambahUrlFile($item));

        return view('arsip', [
            'user' => $user,
            'daftarArsip' => $daftarArsip,
        ]);
    }

    public function getData(Request $request)
    {
        $user = auth()->user();
        $daftarArsip = $this->queryArsipBookmark($user->id_user, $request)
            ->get()
            ->map(function ($item) {
                $item = $this->tambahUrlFile($item);
                $item->waktuUnggahFormatted = Carbon::parse($item->waktu_unggah)->translatedFormat('j F Y');
                return $item;
            });

        return response()->json([
            'status' => 'success',
            'data' => [
                'user' => $user,
                'daftarArsip' => $daftarArsip,
            ]
        ]);
    }

    public function toggleBookmark(Request $request, $id)
    {
        $user = auth()->user();

        $dokumen = DB::table('dokumen')
            ->where('id_dokumen', $id)
            ->where('status', 'disetujui')
            ->first();

        if (!$dokumen) {
            return response()->json(['message' => 'Dokumen tidak ditemukan.'], 404);
        }

        $sudahDibookmark = DB::table('bookmark')
            ->where('id_user', $user->id_user)
            ->where('id_dokumen', $id)
            ->exists();

        if ($sudahDibookmark) {
            DB::table('bookmark')
                ->where('id_user', $user->id_user)
                ->where('id_dokumen', $id)
                ->delete();

            return response()->json([
                'status' => 'success',
                'bookmarked' => false,
                'message' => 'Bookmark dihapus.',
            ]);
        }

        DB::table('bookmark')->insert([
            'id_user' => $user->id_user,
            'id_dokumen' => $id,
        ]);

        return response()->json([
            'status' => 'success',
            'bookmarked' => true,
            'message' => 'Dokumen disimpan ke arsip.',
        ]);
    }

    // --- Private Methods ---

    private function queryArsipBookmark(int $idUser, Request $request)
    {
        $tahun = $request->query('tahun');
        $cari = $request->query('q', '');

        $arsipQuery = DB::table('bookmark')
            ->join('dokumen', 'bookmark.id_dokumen', '=', 'dokumen.id_dokumen')
            ->join('mata_kuliah', 'dokumen.id_matkul', '=', 'mata_kuliah.id_matkul')
            ->join('dosen', 'dokumen.id_dosen', '=', 'dosen.id_dosen')
            ->where('bookmark.id_user', $idUser)
            ->where('dokumen.status', 'disetujui')
            ->select(
                'dokumen.id_dokumen',
                'dokumen.judul',
                'dokumen.kategori_file',
                'dokumen.tahun_dokumen',
                'dokumen.waktu_unggah',
                'dokumen.file_path',
                'mata_kuliah.nama_matkul',
                'dosen.nama_dosen'
            );

        if ($tahun) {
            $arsipQuery->where('dokumen.tahun_dokumen', $tahun);
        }

        if ($cari) {
            $arsipQuery->whereRaw('LOWER(dokumen.judul) REGEXP ?', ['(^|[[:space:]])' . strtolower(preg_quote($cari))]);
        }

        return $arsipQuery->orderBy('dokumen.waktu_unggah', 'desc');
    }

    private function tambahUrlFile(object $item): object
    {
        $item->file_url = route('arsip.view', [
            'kode' => Crypt::encryptString($item->id_dokumen)
        ]);
        return $item;
    }

    public function viewDokumen($kode)
    {
        try {
            // 1. Buka gembok kode rahasia dari Android
            $idDokumen = Crypt::decryptString($kode);

            // 2. Cari file-nya di database
            $dokumen = DB::table('dokumen')->where('id_dokumen', $idDokumen)->first();

            if (!$dokumen) {
                return response()->json(['message' => 'Dokumen tidak ditemukan di database.'], 404);
            }

            // 3. Cari fisik file-nya di folder laptopmu
            // (Pastikan 'app/public/' ini sesuai dengan tempat aslimu menyimpan file upload)
            $pathToFile = storage_path('app/public/' . $dokumen->file_path);

            if (!file_exists($pathToFile)) {
                return response()->json(['message' => 'File fisik tidak ditemukan di folder laptop.'], 404);
            }

            // 4. KUNCI UTAMA: Kembalikan murni sebagai FILE, bukan tulisan/HTML!
            return response()->file($pathToFile);

        } catch (\Illuminate\Contracts\Encryption\DecryptException $e) {
            return response()->json(['message' => 'Kode rahasia tidak valid.'], 400);
        }
    }
}
