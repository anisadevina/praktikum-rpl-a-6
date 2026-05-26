<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\User;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;

class MatkulController extends Controller
{
    public function search(Request $request)
    {
        $query = $request->input('q', '');

        $matkul = DB::table('mata_kuliah')
            ->where('nama_matkul', 'like', '%' . $query . '%')
            ->get()
            ->map(function ($item) {
                $item->arsip = DB::table('dokumen')->where('id_matkul', $item->id_matkul)->count();
                return $item;
            });

        return response()->json($matkul);
    }

    public function matkul(Request $request)
    {
        $user = auth()->user();
        $query = $request->input('q', '');

        $semuaMatkul = DB::table('mata_kuliah')
            ->when($query, function ($q) use ($query) {
                $q->where('nama_matkul', 'like', '%' . $query . '%');
            })
            ->get()
            ->map(function ($item) {
                $item->arsip = DB::table('dokumen')->where('id_matkul', $item->id_matkul)->count();
                return $item;
            });

        return view('matkul', [
            'user' => $user,
            'semuaMatkul' => $semuaMatkul,
            'query' => $query,
        ]);
    }
}
