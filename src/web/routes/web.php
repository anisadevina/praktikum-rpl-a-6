<?php

use App\Http\Controllers\authController;
use App\Http\Controllers\MatkulController;
use App\Http\Controllers\ForumController;
use Illuminate\Support\Facades\Route;
use Illuminate\Support\Facades\DB;

// -- AUTHEHTIKASI --
Route::get('/', function () {
    return view('login');
})->name('login');

Route::get('/register', function () {
    return view('register');
})->name('register');

Route::post('/login', [AuthController::class, 'login'])->name('login.proses');
Route::post('/register', [AuthController::class, 'register'])->name('register.proses');
Route::post('/logout', [AuthController::class, 'logout'])->name('logout');

// -- BERANDA --
Route::get('/beranda', function () {
    $user = auth()->user();

    // ambil 4 mata kuliah terakhir yang diakses oleh user
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

    $forumTerbaru = DB::table('forum_topik')
        ->join('users', 'forum_topik.id_user', '=', 'users.id_user')
        ->select('forum_topik.*', 'users.username')
        ->orderBy('forum_topik.waktu_topik', 'desc')
        ->limit(4)
        ->get();

    return view('beranda', [
        'user' => $user,
        'mataKuliahTerakhir' => $mataKuliahTerakhir,
        'forumTerbaru' => $forumTerbaru,
    ]);
})->name('beranda')->middleware('auth');

// -- MATKUL --
Route::get('/search', [MatkulController::class, 'search'])->name('search')->middleware('auth');
Route::get('/matkul', [MatkulController::class, 'matkul'])->name('matkul')->middleware('auth');

Route::get('/matkul/detail', function (\Illuminate\Http\Request $request) {
    $id_matkul = $request->query('id');
    $user = auth()->user();

    // Jika tidak ada ID, kembalikan ke beranda
    if (!$id_matkul) {
        return redirect('/beranda');
    }

    // Catat riwayat akses 
    if ($user) {
        DB::table('riwayat_akses')->updateOrInsert(
            [
                'id_user' => $user->id_user,
                'id_matkul' => $id_matkul
            ],
            [
                'waktu_akses' => \Carbon\Carbon::now()
            ]
        );
    }

    //Ambil data Mata Kuliah dari database
    $matkul = DB::table('mata_kuliah')->where('id_matkul', $id_matkul)->first();

    // Jika matkul tidak ditemukan di database, kembalikan ke beranda
    if (!$matkul) {
        return redirect('/beranda');
    }
    // mengonversi tingkat kesulitan ke teks
    $skor = $matkul->tingkat_kesulitan;
    $teksKesulitan = 'Belum dinilai';

    if ($skor >= 1.00 && $skor < 2.00) {
        $teksKesulitan = 'Materi cenderung sulit sekali';
    } elseif ($skor >= 2.00 && $skor < 3.00) {
        $teksKesulitan = 'Materi cenderung sulit';
    } elseif ($skor >= 3.00 && $skor < 4.00) {
        $teksKesulitan = 'Materi cenderung sedang';
    } elseif ($skor >= 4.00 && $skor < 5.00) {
        $teksKesulitan = 'Materi cenderung mudah';
    } elseif ($skor >= 5.00) {
        $teksKesulitan = 'Materi cenderung mudah sekali';
    }

    // Tangkap parameter 'tahun' dari URL jika ada
    $tahunFilter = $request->query('tahun');

    // Buat "kerangka" query dasar untuk arsip matkul ini
    $queryArsip = DB::table('dokumen')->where('id_matkul', $id_matkul);

    // Jika user memilih tahun (parameter tahun tidak kosong), tambahkan filter
    if (!empty($tahunFilter)) {
        $queryArsip->where('tahun_dokumen', $tahunFilter);
    }

    // Eksekusi query untuk mendapatkan jumlah dan daftar arsip
    $jumlahArsip = $queryArsip->count();
    $daftarArsip = $queryArsip->get();

    // Kirim data ke view detailMatkul
    return view('detailMatkul', [
        'user' => $user,
        'matkul' => $matkul,
        'jumlahArsip' => $jumlahArsip,
        'daftarArsip' => $daftarArsip,
        'teksKesulitan' => $teksKesulitan
    ]);

})->name('matkul.detail')->middleware('auth');

// -- FORUM --
Route::get('/forum', [ForumController::class, 'forum'])->name('forum')->middleware('auth');
Route::post('/forum/topik', [ForumController::class, 'buatTopik'])->name('forum.topik')->middleware('auth');
Route::post('/forum/balasan', [ForumController::class, 'buatBalasan'])->name('forum.balasan')->middleware('auth');