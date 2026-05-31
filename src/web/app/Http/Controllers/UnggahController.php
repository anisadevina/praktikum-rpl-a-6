<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Carbon\Carbon;

class UnggahController extends Controller
{
    // halaman kosong
    public function index()
    {
        return view('unggah');
    }

    // kirim data dan riwayat unggah user
    public function getData(Request $request)
    {
        $user = auth()->user();

        $mataKuliah = DB::table('mata_kuliah')->orderBy('nama_matkul')->get();
        $dosen = DB::table('dosen')->orderBy('nama_dosen')->get();

        $dokumenUser = DB::table('dokumen')
            ->join('mata_kuliah', 'dokumen.id_matkul', '=', 'mata_kuliah.id_matkul')
            ->where('dokumen.id_user', $user->id_user)
            ->select('dokumen.*', 'mata_kuliah.nama_matkul')
            ->orderBy('dokumen.waktu_unggah', 'desc')
            ->get();

        return response()->json([
            'status' => 'success',
            'data' => [
                'user' => $user,
                'mataKuliah' => $mataKuliah,
                'dosen' => $dosen,
                'dokumen' => $dokumenUser,
            ]

        ]);


    }

    public function upload(Request $request)
    {
        $user = auth()->user();

        $request->validate([
            'id_matkul' => 'required|exists:mata_kuliah,id_matkul',
            'tahun' => 'required|integer|min:2000|max:' . (Carbon::now()->year + 1),
            'id_dosen' => 'required|exists:dosen,id_dosen',
            'kategori' => 'required|in:soal ujian,tugas,materi',
            'judul_file' => 'required|string|max:255',
            'file_pdf' => 'required|file|mimes:pdf|max:10240',
        ]);

        if ($request->hasFile('file_pdf')) {
            $file = $request->file('file_pdf');

            $path = $file->store('arsip', 'public');

            DB::table('dokumen')->insert([
                'id_user' => $user->id_user,
                'id_matkul' => $request->id_matkul,
                'id_dosen' => $request->id_dosen,
                'judul' => $request->judul_file,
                'kategori_file' => $request->kategori,
                'tahun_dokumen' => $request->tahun,
                'file_path' => $path,
                'status' => 'menunggu',
                'waktu_unggah' => Carbon::now(),
            ]);

            return response()->json([
                'status' => 'success',
                'message' => 'File berhasil diunggah'
            ]);
        }

        return response()->json([
            'status' => 'error',
            'message' => 'File gagal diproses'
        ], 400);
    }
}
