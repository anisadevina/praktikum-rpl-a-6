<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Storage;

class ReviewDokumenController extends Controller
{
    public function index()
    {
        return view('reviewList');
    }

    public function detail($id)
    {
        return view('reviewDetail', ['id' => $id]);
    }

    public function getListData()
    {
        try {
            $dokumen = DB::table('dokumen')
                ->join('mata_kuliah', 'dokumen.id_matkul', '=', 'mata_kuliah.id_matkul')
                ->where('dokumen.status', 'menunggu')
                ->select(
                    'dokumen.id_dokumen',
                    'dokumen.judul',
                    'mata_kuliah.nama_matkul',
                    'dokumen.waktu_unggah',
                    'dokumen.status'
                )
                ->orderBy('dokumen.waktu_unggah', 'desc')
                ->get();

            return response()->json(['status' => 'success', 'data' => $dokumen]);
        } catch (\Exception $e) {
            Log::error('API Review List Error: ' . $e->getMessage());
            return response()->json(['status' => 'error', 'message' => $e->getMessage()], 500);
        }
    }

    public function getDetailData($id)
    {
        try {
            $user = auth()->user();

            $dokumen = DB::table('dokumen')
                ->join('mata_kuliah', 'dokumen.id_matkul', '=', 'mata_kuliah.id_matkul')
                ->leftJoin('dosen', 'dokumen.id_dosen', '=', 'dosen.id_dosen')
                ->where('dokumen.id_dokumen', $id)
                ->select('dokumen.*', 'mata_kuliah.nama_matkul', 'dosen.nama_dosen')
                ->first();

            if (!$dokumen) {
                return response()->json(['status' => 'error', 'message' => 'Dokumen tidak ditemukan'], 404);
            }

            return response()->json(['status' => 'success', 'data' => $dokumen, 'user' => $user]);
        } catch (\Exception $e) {
            Log::error('API Review Detail Error: ' . $e->getMessage());
            return response()->json(['status' => 'error', 'message' => $e->getMessage()], 500);
        }
    }

    public function submitReview(Request $request, $id)
    {
        try {
            $cekDokumen = DB::table('dokumen')->where('id_dokumen', $id)->first();

            if ($cekDokumen && $cekDokumen->status !== 'menunggu') {
                return response()->json([
                    'status' => 'error',
                    'message' => 'Dokumen ini sudah disetujui secara permanen dan tidak bisa diubah lagi!'
                ], 403);
            }

            $request->validate([
                'status_dokumen' => 'required|in:disetujui,ditolak'
            ]);

            $teksMurni = trim(strip_tags($request->catatan_admin));

            if ($request->status_dokumen === 'ditolak' && empty($teksMurni)) {
                return response()->json([
                    'status' => 'error',
                    'message' => 'Catatan wajib diisi jika dokumen ditolak!'
                ], 400);
            }

            $filePathBaru = $this->prosesFileSaatDitolak($cekDokumen, $request->status_dokumen);

            DB::table('dokumen')->where('id_dokumen', $id)->update([
                'status' => $request->status_dokumen,
                'catatan_admin' => $request->catatan_admin,
                'file_path' => $filePathBaru
            ]);

            return response()->json(['status' => 'success', 'message' => 'Review berhasil disimpan!']);
        } catch (\Exception $e) {
            Log::error('API Submit Review Error: ' . $e->getMessage());
            return response()->json(['status' => 'error', 'message' => 'Terjadi kesalahan saat menyimpan.'], 500);
        }
    }

    // --- Private Methods ---

    /**
     * Hapus file fisik dari storage jika dokumen ditolak,
     * dan kembalikan nilai file_path yang akan disimpan ke database.
     */
    private function prosesFileSaatDitolak(object $dokumen, string $statusBaru): string
    {
        if ($statusBaru !== 'ditolak') {
            return $dokumen->file_path;
        }

        if ($dokumen->file_path && Storage::disk('public')->exists($dokumen->file_path)) {
            Storage::disk('public')->delete($dokumen->file_path);
        }

        return '';
    }
}
