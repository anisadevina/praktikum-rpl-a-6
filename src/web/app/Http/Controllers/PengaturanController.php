<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Cache;
use Illuminate\Support\Facades\Log;

class PengaturanController extends Controller
{
    // Cek status On/Off
    public function getPopupStatus()
    {
        // Ambil data dari Cache. Jika belum pernah diatur, berikan nilai awal 'off'
        $status = Cache::get('popup_kuisioner', 'off');

        return response()->json(['status' => 'success', 'data' => $status]);
    }

    // Admin mengubah status On/Off
    public function setPopupStatus(Request $request)
    {
        try {
            $user = auth()->user();

            // Pastikan yang mengakses adalah admin
            if (!$user || $user->role !== 'admin') {
                return response()->json(['status' => 'error', 'message' => 'Akses ditolak'], 403);
            }

            $statusBaru = $request->status;

            Cache::forever('popup_kuisioner', $statusBaru);

            return response()->json(['status' => 'success', 'message' => 'Status popup diperbarui!']);

        } catch (\Exception $e) {
            Log::error("Pengaturan API Error: " . $e->getMessage());
            return response()->json(['status' => 'error', 'message' => 'Gagal mengubah pengaturan'], 500);
        }
    }
}