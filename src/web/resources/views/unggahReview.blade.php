<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Review Unggahan — Study Scope</title>
    <link rel="stylesheet" href="{{ asset('style/global.css') }}">
    <link rel="stylesheet" href="{{ asset('style/beranda.css') }}">
    <link rel="stylesheet" href="{{ asset('style/unggahAdmin.css') }}">
    <!-- Quill Rich Text Editor -->
    <link href="https://cdn.quilljs.com/1.3.7/quill.snow.css" rel="stylesheet">
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
            <a href="{{ route('beranda') }}" class="nav-item">
                <div class="nav-item-left">
                    <svg viewBox="0 0 24 24"><rect x="3" y="3" width="7" height="7" rx="1"/><rect x="14" y="3" width="7" height="7" rx="1"/><rect x="14" y="14" width="7" height="7" rx="1"/><rect x="3" y="14" width="7" height="7" rx="1"/></svg>
                    <span>Beranda</span>
                </div>
                <svg class="nav-chevron" viewBox="0 0 24 24"><polyline points="9 18 15 12 9 6"/></svg>
            </a>
            <a href="{{ route('matkul') }}" class="nav-item">
                <div class="nav-item-left">
                    <svg viewBox="0 0 24 24"><path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"/><path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"/></svg>
                    <span>Mata Kuliah</span>
                </div>
                <svg class="nav-chevron" viewBox="0 0 24 24"><polyline points="9 18 15 12 9 6"/></svg>
            </a>
            <a href="{{ route('forum') }}" class="nav-item">
                <div class="nav-item-left">
                    <svg viewBox="0 0 24 24"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/></svg>
                    <span>Forum</span>
                </div>
                <svg class="nav-chevron" viewBox="0 0 24 24"><polyline points="9 18 15 12 9 6"/></svg>
            </a>
            <a href="{{ route('unggah.admin') }}" class="nav-item active">
                <div class="nav-item-left">
                    <svg viewBox="0 0 24 24"><polyline points="16 16 12 12 8 16"/><line x1="12" y1="12" x2="12" y2="21"/><path d="M20.39 18.39A5 5 0 0 0 18 9h-1.26A8 8 0 1 0 3 16.3"/></svg>
                    <span>Unggah</span>
                </div>
                <svg class="nav-chevron" viewBox="0 0 24 24"><polyline points="9 18 15 12 9 6"/></svg>
            </a>
            <div class="nav-item">
                <div class="nav-item-left">
                    <svg viewBox="0 0 24 24"><path d="M19 21l-7-5-7 5V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2z"/></svg>
                    <span>Arsip</span>
                </div>
                <svg class="nav-chevron" viewBox="0 0 24 24"><polyline points="9 18 15 12 9 6"/></svg>
            </div>
        </nav>

        <div class="sidebar-footer">
            <form method="POST" action="{{ route('logout') }}" id="form-logout">
                @csrf
                <div class="nav-item" onclick="document.getElementById('form-logout').submit()" style="cursor:pointer;">
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
                <input type="text" placeholder="Cari mata kuliah" autocomplete="off"/>
            </div>
            <div class="topbar-user">
                <div class="topbar-avatar">
                    <svg viewBox="0 0 24 24"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
                </div>
                <span class="topbar-username">{{ $user->username ?? 'namapengguna' }}</span>
            </div>
        </header>

        <main class="page-scroll">

            <div class="page-view-container">

                <div class="unggah-page-header">
                    <div class="unggah-header-left">
                        <div class="unggah-title-row">
                            <svg width="26" height="26" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                <polyline points="16 16 12 12 8 16"/>
                                <line x1="12" y1="12" x2="12" y2="21"/>
                                <path d="M20.39 18.39A5 5 0 0 0 18 9h-1.26A8 8 0 1 0 3 16.3"/>
                            </svg>
                            <h1 class="unggah-title">Unggah</h1>
                        </div>
                        <p class="unggah-subtitle">Detail Permintaan Unggah.</p>
                    </div>
                </div>

                {{-- Grid 2 kolom --}}
                <div class="review-grid">

                    {{-- Kiri Atas: Informasi File --}}
                    <div class="review-card">
                        <h3 class="review-card-title">Informasi File</h3>
                        <div class="info-item">
                            <span class="info-label">Mata Kuliah</span>
                            <span class="info-value">{{ $dokumen->nama_matkul ?? 'NamaMataKuliah' }}</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">Tahun</span>
                            <span class="info-value">{{ $dokumen->tahun ?? '2026' }}</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">Dosen</span>
                            <span class="info-value">{{ $dokumen->nama_dosen ?? 'Nama Dosen' }}</span>
                        </div>
                    </div>

                    {{-- Kanan Atas: Catatan Review --}}
                    <div class="review-card">
                        <h3 class="review-card-title">Catatan Review</h3>
                        <div id="quill-editor" class="quill-editor-area"></div>
                        <div class="quill-counter">
                            <span id="quill-char-count">0</span>/1000
                        </div>
                        {{-- Hidden input untuk kirim nilai catatan --}}
                        <input type="hidden" name="catatan" id="catatan-hidden">
                    </div>

                    {{-- Kiri Bawah: Preview File PDF --}}
                    <div class="review-card">
                        <h3 class="review-card-title">Preview File (PDF)</h3>
                        <a href="{{ route('lihat-dokumen', ['id' => $dokumen->id_dokumen ?? 0]) }}" 
                            class="pdf-preview-area" 
                            style="text-decoration:none;">
                            <svg width="36" height="36" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
                                <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
                                <polyline points="14 2 14 8 20 8"/>
                            </svg>
                            <p>Preview PDF</p>
                        </a>
                        {{-- Iframe akan muncul saat ada file --}}
                        @isset($dokumen->file_path)
                        <iframe
                            src="{{ asset('storage/' . $dokumen->file_path) }}"
                            class="pdf-iframe"
                            title="Preview PDF">
                        </iframe>
                        @endisset
                    </div>

                    {{-- Kanan Bawah: Status --}}
                    <div class="review-card">
                        <h3 class="review-card-title">Status</h3>
                        <div class="status-options">
                            <label class="status-radio-label" id="label-setujui">
                                <input type="radio" name="keputusan" value="disetujui" id="radio-setujui" checked>
                                <span>Setujui</span>
                            </label>
                            <label class="status-radio-label" id="label-tolak">
                                <input type="radio" name="keputusan" value="ditolak" id="radio-tolak">
                                <span>Tolak</span>
                            </label>
                        </div>
                    </div>

                </div>

                {{-- Tombol Aksi --}}
                <div class="review-actions">
                    <a href="{{ route('unggah.admin') }}" class="btn-kembali">Kembali</a>
                    <button type="button" class="btn-kirim-review" id="btn-kirim-review"
                        data-id="{{ $dokumen->id_dokumen ?? '' }}"
                        data-action="{{ route('unggah.keputusan', $dokumen->id_dokumen ?? 0) }}">
                        Kirim
                    </button>
                </div>

            </div>

        </main>
    </div>

</div>

<script src="https://cdn.quilljs.com/1.3.7/quill.min.js"></script>
<script src="{{ asset('script/unggahReview.js') }}"></script>
</body>
</html>