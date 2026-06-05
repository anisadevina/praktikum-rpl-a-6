<?php

namespace App\Http\Middleware;

use Closure;
use Illuminate\Http\Request;
use Symfony\Component\HttpFoundation\Response;

class CekAdmin
{
    /**
     * Handle an incoming request.
     *
     * @param  Closure(Request): (Response)  $next
     */
    public function handle(Request $request, Closure $next): Response
    {
        if (!auth()->check()) {
            return redirect()->route('login');
        }

        // 2. Cek apakah role-nya adalah 'admin'
        if (auth()->user()->role === 'admin') {
            return $next($request);
        }

        // Memunculkan halaman error 403 (Forbidden / Akses Ditolak)
        abort(403, 'Akses Ditolak. Halaman ini khusus Administrator.');
    }
}
