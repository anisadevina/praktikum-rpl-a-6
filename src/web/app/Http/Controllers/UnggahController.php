<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Carbon\Carbon;
use Illuminate\Support\Facades\Log;

class UnggahController extends Controller
{
    // Halaman daftar unggahan user
    public function index()
    {
        return view('unggahList');
    }

    // Halaman form unggah file
    public function create()
    {
        return view('unggahDetail', [
            'user' => auth()->user(),
        ]);
    }

    // API: ambil data dropdown + riwayat unggahan user
    public function getData(Request $request)
    {
        $user = auth()->user();

        $search = $request->query('q', '');

        $mataKuliah = DB::table('mata_kuliah')->orderBy('nama_matkul')->get();
        $dosen = DB::table('dosen')->orderBy('nama_dosen')->get();

        $query = DB::table('dokumen')
            ->join('mata_kuliah', 'dokumen.id_matkul', '=', 'mata_kuliah.id_matkul')
            ->where('dokumen.id_user', $user->id_user)
            ->select('dokumen.*', 'mata_kuliah.nama_matkul');

        if (!empty($search)) {
            $query->where(function ($q) use ($search) {
                $q->where('mata_kuliah.nama_matkul', 'LIKE', '%' . $search . '%')
                    ->orWhere('dokumen.judul', 'LIKE', '%' . $search . '%');
            });
        }

        $dokumenUser = $query->orderBy('dokumen.waktu_unggah', 'desc')->get();

        return response()->json([
            'status' => 'success',
            'data' => [
                'user' => $user,
                'mataKuliah' => $mataKuliah,
                'dosen' => $dosen,
                'dokumen' => $dokumenUser,
            ],
        ]);
    }

    // Upload file
    public function upload(Request $request)
    {
        $user = auth()->user();

        $request->validate([
            'id_matkul' => 'required|exists:mata_kuliah,id_matkul',
            'tahun' => 'required|integer|min:2000|max:' . (Carbon::now()->year + 1),
            'id_dosen' => 'required|exists:dosen,id_dosen',
            'kategori_file' => 'required|in:soal_ujian,tugas,materi',
            'judul' => 'required|string|max:255',
            'file_pdf' => 'required|file|mimes:pdf|max:20480',
        ]);

        if ($request->hasFile('file_pdf')) {
            try {
                $path = $request->file('file_pdf')->store('arsip', 'public');

                $namaDosen = DB::table('dosen')->where('id_dosen', $request->id_dosen)->value('nama_dosen');

                DB::table('dokumen')->insert([
                    'id_user' => $user->id_user,
                    'id_matkul' => $request->id_matkul,
                    'id_dosen' => $request->id_dosen,
                    'judul' => $request->judul,
                    'kategori_file' => $request->kategori_file,
                    'tahun_dokumen' => $request->tahun,
                    'file_path' => $path,
                    'status' => 'menunggu',
                    'waktu_unggah' => Carbon::now(),
                ]);

                return response()->json([
                    'status' => 'success',
                    'message' => 'File berhasil diunggah, menunggu review.',
                ]);

            } catch (\Exception $e) {
                Log::error('Upload API Error: ' . $e->getMessage());
                return response()->json([
                    'status' => 'error',
                    'message' => 'Terjadi kesalahan saat mengunggah file: ' . $e->getMessage(),
                ], 500);
            }



        }

        return response()->json([
            'status' => 'error',
            'message' => 'File gagal diproses.',
        ], 400);
    }
}