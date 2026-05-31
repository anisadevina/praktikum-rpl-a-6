<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="csrf-token" content="{{ csrf_token() }}">
    <title>Forum — Study Scope</title>
    <link rel="stylesheet" href="{{ asset('style/global.css') }}">
    <link rel="stylesheet" href="{{ asset('style/beranda.css') }}">
    <link rel="stylesheet" href="{{ asset('style/forum.css') }}">
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
            <a href="{{ route('forum') }}" class="nav-item active" data-page="forum">
                <div class="nav-item-left">
                    <svg viewBox="0 0 24 24"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/></svg>
                    <span>Forum</span>
                </div>
                <svg class="nav-chevron" viewBox="0 0 24 24"><polyline points="9 18 15 12 9 6"/></svg>
            </a>
            <div class="nav-item" data-page="unggah">
                <div class="nav-item-left">
                    <svg viewBox="0 0 24 24"><polyline points="16 16 12 12 8 16"/><line x1="12" y1="12" x2="12" y2="21"/><path d="M20.39 18.39A5 5 0 0 0 18 9h-1.26A8 8 0 1 0 3 16.3"/></svg>
                    <span>Unggah</span>
                </div>
                <svg class="nav-chevron" viewBox="0 0 24 24"><polyline points="9 18 15 12 9 6"/></svg>
            </div>
            <div class="nav-item" data-page="arsip">
                <div class="nav-item-left">
                    <svg viewBox="0 0 24 24"><path d="M19 21l-7-5-7 5V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2z"/></svg>
                    <span>Arsip</span>
                </div>
                <svg class="nav-chevron" viewBox="0 0 24 24"><polyline points="9 18 15 12 9 6"/></svg>
            </div>
        </nav>

        <div class="sidebar-footer">
            <form method="POST" action="{{ route('logout') }}" id="logout-form">
                @csrf
                <div class="nav-item" id="btn-keluar" style="cursor:pointer;">
                    <div class="nav-item-left">
                        <svg viewBox="0 0 24 24"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></svg>
                        <span>Keluar</span>
                    </div>
                    <svg class="nav-chevron" viewBox="0 0 24 24"><polyline points="9 18 15 12 9 6"/></svg>
                </div>
            </form>
        </div>
    </aside>

    <div class="main-content">
        <header class="topbar">
            <div class="search-bar">
                <svg viewBox="0 0 24 24"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>
                <input type="text" id="search-input" placeholder="Cari topik forum" autocomplete="off"/>
            </div>
            <div class="topbar-user">
                <div class="topbar-avatar">
                    <svg viewBox="0 0 24 24"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
                </div>
                <span class="topbar-username" id="topbar-username">...</span>
            </div>
        </header>

        <main class="page-scroll">

            {{-- HALAMAN DAFTAR FORUM --}}
            <section id="forum-view" class="page-view-container">
                <div class="forum-header">
                    <div class="header-text">
                        <h1>Forum</h1>
                        <p>Buat topik yang ingin dibicarakan, dan mulai berinteraksi dengan semua orang.</p>
                    </div>
                    <button class="btn-create-topic" id="btn-go-to-create">+ Buat Topik</button>
                </div>

                <h3 class="section-title">Terbaru</h3>

                {{-- Diisi oleh JS --}}
                <div id="forum-feed-container" class="feed-wrapper">
                    <p style="color:#888; font-style:italic; padding:1rem 0;">Memuat topik...</p>
                </div>
            </section>

            {{-- HALAMAN BUAT TOPIK --}}
            <section id="create-topic-view" class="page-view-container hidden">
                <div class="forum-header">
                    <div class="header-text">
                        <h1>Forum</h1>
                        <p>Buat topik yang ingin kamu bicarakan, dan mulai berinteraksi dengan semua orang.</p>
                    </div>
                </div>

                <div class="create-card">
                    <h2>Buat Topik Baru</h2>
                    <div id="form-error" style="color:red; font-size:13px; margin-bottom:8px;"></div>
                    <div class="form-group">
                        <label>Pilih Kategori</label>
                        <div class="category-options">
                            <button type="button" class="category-btn active" data-cat="tanya jawab">Tanya Jawab Soal</button>
                            <button type="button" class="category-btn" data-cat="general">General</button>
                        </div>
                        <input type="hidden" id="input-tag" value="tanya jawab">
                    </div>
                    <div class="form-group">
                        <label>Pesan</label>
                        <textarea id="topic-message" placeholder="Tulis pertanyaan atau topik Anda di sini...." maxlength="2000" autocomplete="off"></textarea>
                        <div class="char-counter" id="char-counter">0/2000</div>
                        <span class="client-error" id="error-pesan" style="color:red; font-size:13px; display:none;"></span>
                    </div>
                    <div class="form-bottom">
                        <div class="privacy-setting">
                            <label>Tampilkan Sebagai</label>
                            <div class="radio-group">
                                <label class="radio-label">
                                    <input type="radio" name="is_anonim" value="0" checked>
                                    <span class="radio-text">
                                        <strong>Nama pengguna Saya</strong><br><small>Username Anda ditampilkan di topik ini</small>
                                    </span>
                                </label>
                                <label class="radio-label">
                                    <input type="radio" name="is_anonim" value="1">
                                    <span class="radio-text">
                                        <strong>Anonim</strong><br><small>Nama Anda disembunyikan di topik ini</small>
                                    </span>
                                </label>
                            </div>
                        </div>
                        <div class="form-actions">
                            <button type="button" class="btn-cancel" id="btn-cancel-create">Batal</button>
                            <button type="button" class="btn-submit" id="btn-submit-topik">Unggah</button>
                        </div>
                    </div>
                </div>
            </section>

        </main>
    </div>

</div>

<script src="{{ asset('script/forum.js') }}"></script>
</body>
</html>