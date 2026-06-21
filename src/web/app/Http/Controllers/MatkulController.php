<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Concerns\AmbilDataMatkul;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Carbon\Carbon;
use Illuminate\Support\Facades\Crypt;

class MatkulController extends Controller
{
    use AmbilDataMatkul;

    public function index()
    {
        return view('matkul');
    }

    public function getIndexData(Request $request)
    {
        $user  = auth()->user();
        $query = $request->input('q', '');

        $mataKuliahTerakhir = $this->ambilMatkulTerakhirDiakses($user->id_user);

        $semuaMatkul = DB::table('mata_kuliah')
            ->when($query, function ($q) use ($query) {
                $q->where('nama_matkul', 'like', '%' . $query . '%');
            })
            ->paginate(12);

        $semuaMatkul->getCollection()->transform(function ($item) {
            return $this->sisipkanJumlahArsip($item);
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
        $idMatkul = $request->query('id');
        $user     = auth()->user();

        if (!$idMatkul) {
            return response()->json([
                'status' => 'error',
                'message' => 'ID tidak ditemukan'
            ], 400);
        }

        $this->catatRiwayatAkses($user->id_user, $idMatkul);

        $matkul = DB::table('mata_kuliah')->where('id_matkul', $idMatkul)->first();

        if (!$matkul) {
            return response()->json([
                'status' => 'error',
                'message' => 'Mata kuliah tidak ditemukan'
            ], 404);
        }

        $teksKesulitan = $this->konversiTingkatKesulitan($matkul->tingkat_kesulitan);

        $jumlahArsip = DB::table('dokumen')
            ->where('id_matkul', $idMatkul)
            ->where('status', 'disetujui')
            ->count();

        $daftarArsip = $this->ambilArsipMatkul($idMatkul, $user->id_user, $request->query('tahun'));

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

    public function viewArsip($kode)
    {
        try {
            $idDokumen = Crypt::decryptString($kode);
        } catch (\Illuminate\Contracts\Encryption\DecryptException $e) {
            abort(404, 'Tautan dokumen tidak valid atau sudah kadaluarsa.');
        }

        $dokumen = DB::table('dokumen')->where('id_dokumen', $idDokumen)->first();

        if (!$dokumen || !$dokumen->file_path) {
            abort(404, 'Arsip tidak ditemukan.');
        }

        $pathFisik = storage_path('app/public/' . $dokumen->file_path);

        if (!file_exists($pathFisik)) {
            abort(404, 'File fisik tidak tersedia di server.');
        }

        return response()->file($pathFisik);
    }

    // --- Private Methods ---

    private function catatRiwayatAkses(int $idUser, int $idMatkul): void
    {
        DB::table('riwayat_akses')->updateOrInsert(
            ['id_user' => $idUser, 'id_matkul' => $idMatkul],
            ['waktu_akses' => Carbon::now()]
        );
    }

    private function ambilArsipMatkul(int $idMatkul, int $idUser, ?string $tahunFilter)
    {
        $idDokumenDibookmark = DB::table('bookmark')
            ->where('id_user', $idUser)
            ->pluck('id_dokumen')
            ->toArray();

        $queryArsip = DB::table('dokumen')
            ->where('id_matkul', $idMatkul)
            ->where('status', 'disetujui')
            ->orderBy('waktu_unggah', 'desc');

        if (!empty($tahunFilter)) {
            $queryArsip->where('tahun_dokumen', $tahunFilter);
        }

        return $queryArsip->get()->map(function ($dokumen) use ($idDokumenDibookmark) {
            $dokumen->kodeRahasia  = Crypt::encryptString($dokumen->id_dokumen ?? $dokumen->id);
            $dokumen->is_bookmarked = in_array($dokumen->id_dokumen, $idDokumenDibookmark);
            return $dokumen;
        });
    }

    private function konversiTingkatKesulitan(float $skor): string
    {
        return match(true) {
            $skor >= 1.00 && $skor < 2.00 => 'Materi cenderung mudah sekali',
            $skor >= 2.00 && $skor < 3.00 => 'Materi cenderung mudah',
            $skor >= 3.00 && $skor < 4.00 => 'Materi cenderung sedang',
            $skor >= 4.00 && $skor < 5.00 => 'Materi cenderung sulit',
            default                        => 'Materi cenderung sulit sekali',
        };
    }
}