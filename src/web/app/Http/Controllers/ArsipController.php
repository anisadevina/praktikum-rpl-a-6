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
            ->map(function ($item) {
                $item->file_url = route('arsip.view', [
                    'kode' => Crypt::encryptString($item->id_dokumen)
                ]);
                return $item;
            });

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
                $item->kodeRahasia = Crypt::encryptString($item->id_dokumen);
                $item->file_url = route('arsip.view', ['kode' => $item->kodeRahasia]);
                $item->waktu_unggah_human = Carbon::parse($item->waktu_unggah)->translatedFormat('j F Y');
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

        $existing = DB::table('bookmark')
            ->where('id_user', $user->id_user)
            ->where('id_dokumen', $id)
            ->first();

        if ($existing) {
            DB::table('bookmark')
                ->where('id_user', $user->id_user)
                ->where('id_dokumen', $id)
                ->delete();

            return response()->json([
                'status' => 'success',
                'bookmarked' => false,
                'message' => 'Bookmark dihapus.'
            ]);
        } else {
            DB::table('bookmark')->insert([
                'id_user' => $user->id_user,
                'id_dokumen' => $id,
            ]);

            return response()->json([
                'status' => 'success',
                'bookmarked' => true,
                'message' => 'Dokumen disimpan ke arsip.'
            ]);
        }
    }

    // --- Private Methods ---

    /**
     * Bangun query dasar arsip yang sudah di-bookmark oleh user,
     * dengan filter tahun dan pencarian judul opsional.
     */
    private function queryArsipBookmark(int $idUser, Request $request)
    {
        $tahun = $request->query('tahun');
        $query = $request->query('q', '');

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

        if ($query) {
            $arsipQuery->whereRaw('LOWER(dokumen.judul) REGEXP ?', ['(^|[[:space:]])' . strtolower(preg_quote($query))]);
        }

        return $arsipQuery->orderBy('dokumen.waktu_unggah', 'desc');
    }
}
