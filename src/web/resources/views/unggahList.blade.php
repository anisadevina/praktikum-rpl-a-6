<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="csrf-token" content="{{ csrf_token() }}">
    <title>Unggah — Study Scope</title>
    <link rel="stylesheet" href="{{ asset('style/global.css') }}">
    <link rel="stylesheet" href="{{ asset('style/beranda.css') }}">
    <link rel="stylesheet" href="{{ asset('style/unggahList.css') }}">
</head>
<body>

<div class="app-layout">

    <aside class="sidebar">
        <div class="sidebar-logo">
            <div class="sidebar-logo-icon">
                <svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" fill="none"/>
                </svg>
            </div>
            <span class="sidebar-logo-name">Study Scope</span>
        </div>

        <nav class="sidebar-nav">
            <a href="{{ route('beranda') }}" class="nav-item" data-page="beranda">
                <div class="nav-item-left">
                    <svg viewBox="0 0 24 24"><rect x="3" y="3" width="7" height="7" rx="1"/><rect x="14" y="3" width="7" height="7" rx="1"/><rect x="14" y="14" width="7" height="7" rx="1"/><rect x="3" y="14" width="7" height="7" rx="1"/></svg>
                    <span>Beranda</span>
                </div>
                <svg class="nav-chevron" viewBox="0 0 24 24"><polyline points="9 18 15 12 9 6"/></svg>
            </a>
            <a href="{{ route('matkul') }}" class="nav-item" data-page="matkul">
                <div class="nav-item-left">
                    <svg viewBox="0 0 24 24"><path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"/><path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"/></svg>
                    <span>Mata Kuliah</span>
                </div>
                <svg class="nav-chevron" viewBox="0 0 24 24"><polyline points="9 18 15 12 9 6"/></svg>
            </a>
            <a href="{{ route('forum') }}" class="nav-item" data-page="forum">
                <div class="nav-item-left">
                    <svg viewBox="0 0 24 24"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/></svg>
                    <span>Forum</span>
                </div>
                <svg class="nav-chevron" viewBox="0 0 24 24"><polyline points="9 18 15 12 9 6"/></svg>
            </a>
            <a href="{{ route('unggah') }}" class="nav-item active" data-page="unggah">
                <div class="nav-item-left">
                    <svg viewBox="0 0 24 24"><polyline points="16 16 12 12 8 16"/><line x1="12" y1="12" x2="12" y2="21"/><path d="M20.39 18.39A5 5 0 0 0 18 9h-1.26A8 8 0 1 0 3 16.3"/></svg>
                    <span>Unggah</span>
                </div>
                <svg class="nav-chevron" viewBox="0 0 24 24"><polyline points="9 18 15 12 9 6"/></svg>
            </a>
            
            <a href="{{ route('arsip') }}" class="nav-item" data-page="arsip">
                <div class="nav-item-left">
                    <svg viewBox="0 0 24 24"><path d="M19 21l-7-5-7 5V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2z"/></svg>
                    <span>Arsip</span>
                </div>
                <svg class="nav-chevron" viewBox="0 0 24 24"><polyline points="9 18 15 12 9 6"/></svg>
            </a>

            <a href="{{ route('unggah.admin') }}" class="nav-item hidden" id="menu-review-admin" style="display: none;" data-page="review">
                <div class="nav-item-left">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <path d="M14 9V5a3 3 0 0 0-3-3l-4 9v11h11.28a2 2 0 0 0 2-1.7l1.38-9a2 2 0 0 0-2-2.3zM7 22H4a2 2 0 0 1-2-2v-7a2 2 0 0 1 2-2h3"></path>
                    </svg>
                    <span>Review Dokumen</span>
                </div>
                <svg class="nav-chevron" viewBox="0 0 24 24"><polyline points="9 18 15 12 9 6"/></svg>
            </a>
        </nav>

        <div class="sidebar-footer">
            <div class="nav-item" id="btn-keluar" style="cursor:pointer;">
                <div class="nav-item-left">
                    <svg viewBox="0 0 24 24"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></svg>
                    <span>Keluar</span>
                </div>
                <svg class="nav-chevron" viewBox="0 0 24 24"><polyline points="9 18 15 12 9 6"/></svg>
            </div>
        </div>
    </aside>

    <div class="main-content">
        <header class="topbar">
            <div class="search-bar">
                <svg viewBox="0 0 24 24"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>
                <input type="text" placeholder="Cari mata kuliah" autocomplete="off"/>
            </div>
            <div class="topbar-user">
                <div class="topbar-avatar">
                    <svg viewBox="0 0 24 24"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
                </div>
                <span class="topbar-username">Memuat...</span>
            </div>
        </header>

        <main class="page-scroll">
            <div class="unggah-page-header">
                <div>
                    <div class="unggah-title-row">
                        <svg viewBox="0 0 24 24">
                            <polyline points="16 16 12 12 8 16"/>
                            <line x1="12" y1="12" x2="12" y2="21"/>
                            <path d="M20.39 18.39A5 5 0 0 0 18 9h-1.26A8 8 0 1 0 3 16.3"/>
                        </svg>
                        <h1 class="unggah-title">UNGGAH</h1>
                    </div>
                    <p class="unggah-subtitle">Unggah file dan berbagi dengan semua orang.</p>
                </div>
                <a href="{{ route('unggah.detail') }}" class="btn-tambah-dokumen">
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round">
                        <line x1="12" y1="5" x2="12" y2="19"/>
                        <line x1="5" y1="12" x2="19" y2="12"/>
                    </svg>
                    Dokumen
                </a>
            </div>

            <hr class="unggah-divider">

            <div class="unggah-table-wrapper">
                <table class="unggah-table">
                    <thead>
                        <tr>
                            <th>Nama File</th>
                            <th>Mata Kuliah</th>
                            <th>Tahun</th>
                            <th>Status</th>
                            <th>Catatan</th>
                        </tr>
                    </thead>
                    <tbody id="unggah-table-body">
                        <tr>
                            <td colspan="5" class="td-empty">Memuat data unggahan...</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </main>
    </div>

</div>

<form id="logout-form" action="{{ route('logout') }}" method="POST" style="display:none;">
    @csrf
</form>
<script src="{{ asset('script/unggahList.js') }}"></script>
</body>
</html>