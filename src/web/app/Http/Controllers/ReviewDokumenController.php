<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Log; // Penting untuk melihat catatan error

class ReviewDokumenController extends Controller
{
    // 1. Tampilkan HTML Kosong List
    public function index()
    {
        return view('reviewList');
    }

    // 2. Tampilkan HTML Kosong Detail
    public function detail($id)
    {
        return view('reviewDetail', ['id' => $id]);
    }

    // 3. API: Ambil semua daftar dokumen (List)
    public function getListData()
    {
        try {
            $dokumen = DB::table('dokumen')
                ->join('mata_kuliah', 'dokumen.id_matkul', '=', 'mata_kuliah.id_matkul')
                ->select(
                    'dokumen.id_dokumen',
                    'dokumen.judul',
                    'mata_kuliah.nama_matkul',
                    'dokumen.waktu_unggah',
                    'dokumen.status'
                )
                // Memastikan kolom 'status' merujuk ke tabel dokumen agar tidak ambigu
                ->orderByRaw("FIELD(dokumen.status, 'menunggu', 'disetujui', 'ditolak')")
                ->orderBy('dokumen.waktu_unggah', 'desc')
                ->get();

            return response()->json(['status' => 'success', 'data' => $dokumen]);
        } catch (\Exception $e) {
            Log::error('API Review List Error: ' . $e->getMessage());
            return response()->json(['status' => 'error', 'message' => $e->getMessage()], 500);
        }
    }

    // 4. API: Ambil 1 data dokumen (Detail)
    public function getDetailData($id)
    {
        try {
            $user = auth()->user();

            $dokumen = DB::table('dokumen')
                ->join('mata_kuliah', 'dokumen.id_matkul', '=', 'mata_kuliah.id_matkul')
                ->leftJoin('dosen', 'dokumen.id_dosen', '=', 'dosen.id_dosen')
                ->where('dokumen.id_dokumen', $id)
                ->select('dokumen.*', 'mata_kuliah.nama_matkul')
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

    // 5. API: Proses Submit Review
    public function submitReview(Request $request, $id)
    {
        try {
            $request->validate([
                'status_dokumen' => 'required|in:disetujui,ditolak'
            ]);

            // Cek teks murni (hapus tag html bawaan editor)
            $teksMurni = trim(strip_tags($request->catatan_admin));

            // Validasi Backend: Wajib isi jika ditolak!
            if ($request->status_dokumen === 'ditolak' && empty($teksMurni)) {
                return response()->json([
                    'status' => 'error',
                    'message' => 'Catatan wajib diisi jika dokumen ditolak!'
                ], 400);
            }

            DB::table('dokumen')->where('id_dokumen', $id)->update([
                'status' => $request->status_dokumen,
                'catatan_admin' => $request->catatan_admin // Simpan format HTML dari Quill Editor
            ]);

            return response()->json(['status' => 'success', 'message' => 'Review berhasil disimpan!']);

        } catch (\Exception $e) {
            Log::error('API Submit Review Error: ' . $e->getMessage());
            return response()->json(['status' => 'error', 'message' => 'Terjadi kesalahan saat menyimpan.'], 500);
        }
    }
}