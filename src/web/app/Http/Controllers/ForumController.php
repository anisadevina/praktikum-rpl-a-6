<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\User;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;

class ForumController extends Controller
{
    public function forum(Request $request)
    {
        $user = auth()->user();

        $topik = DB::table('forum_topik')
            ->join('users', 'forum_topik.id_user', '=', 'users.id_user')
            ->select('forum_topik.*', 'users.username')
            ->orderBy('forum_topik.waktu_topik', 'desc')
            ->get()
            ->map(function ($topik) {
                $topik->jumlah_balasan = DB::table('forum_balasan')
                    ->where('id_topik', $topik->id_topik)
                    ->count();
                $topik->balasan = DB::table('forum_balasan')
                    ->join('users', 'forum_balasan.id_user', '=', 'users.id_user')
                    ->where('forum_balasan.id_topik', $topik->id_topik)
                    ->select('forum_balasan.*', 'users.username')
                    ->orderBy('forum_balasan.waktu_balasan', 'asc')
                    ->get();
                return $topik;
            });

        return view('forum', [
            'user' => $user,
            'topik' => $topik,
        ]);
    }

    public function buatTopik(Request $request)
    {
        $request->validate([
            'pesan_topik' => 'required|min:5',
            'tag' => 'required|in:general,tanya jawab',
        ], [
            'pesan_topik.required' => 'Pesan tidak boleh kosong.',
            'pesan_topik.min' => 'Pesan minimal 5 karakter.',
            'tag.required' => 'Pilih kategori terlebih dahulu.',
        ]);

        $user = auth()->user();

        DB::table('forum_topik')->insert([
            'id_user' => $user->id_user,
            'tag' => $request->tag,
            'pesan_topik' => $request->pesan_topik,
            'is_anonim' => $request->is_anonim == '1' ? true : false,
            'waktu_topik' => now(),
        ]);

        return redirect()->route('forum')->with('success', 'Topik berhasil dibuat!');
    }

    public function buatBalasan(Request $request)
    {
        $request->validate([
            'id_topik' => 'required|exists:forum_topik,id_topik',
            'pesan_balasan' => 'required|min:1',
        ], [
            'pesan_balasan.required' => 'Balasan tidak boleh kosong.',
        ]);

        $user = auth()->user();

        DB::table('forum_balasan')->insert([
            'id_topik' => $request->id_topik,
            'id_user' => $user->id_user,
            'pesan_balasan' => $request->pesan_balasan,
            'is_anonim' => $request->is_anonim == '1' ? true : false,
            'waktu_balasan' => now(),
        ]);

        return redirect()->route('forum')->with('success', 'Balasan berhasil dikirim!')->withFragment('topik-' . $request->id_topik);
    }
}
