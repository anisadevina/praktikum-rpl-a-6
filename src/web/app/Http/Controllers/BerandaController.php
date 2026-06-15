<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Concerns\AmbilDataMatkul;
use Illuminate\Support\Facades\DB;

class BerandaController extends Controller
{
    use AmbilDataMatkul;

    public function index()
    {
        return view('beranda');
    }

    public function getData()
    {
        $user = auth()->user();

        $mataKuliahTerakhir = $this->ambilMatkulTerakhirDiakses($user->id_user);

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
                'forumTerbaru' => $forumTerbaru,
            ]
        ]);
    }
}
