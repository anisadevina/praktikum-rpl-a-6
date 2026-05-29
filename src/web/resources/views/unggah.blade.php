<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Unggah — Study Scope</title>
    <link rel="stylesheet" href="{{ asset('style/global.css') }}">
    <link rel="stylesheet" href="{{ asset('style/beranda.css') }}">
    <link rel="stylesheet" href="{{ asset('style/unggah.css') }}">
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
            <div class="nav-item" data-page="arsip">
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

           
            {{-- VIEW 1: DAFTAR FILE DIUNGGAH    --}}
            <section id="unggah-view" class="page-view-container">

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
                        <p class="unggah-subtitle">Unggah file dan berbagi dengan semua orang.</p>
                    </div>
                    <button class="btn-tambah-dokumen" id="btn-go-to-form">
                        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round">
                            <line x1="12" y1="5" x2="12" y2="19"/>
                            <line x1="5" y1="12" x2="19" y2="12"/>
                        </svg>
                        Dokumen
                    </button>
                </div>

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
                            @isset($dokumen)
                                @forelse($dokumen as $d)
                                <tr>
                                    <td class="td-filename">{{ $d->judul_file }}</td>
                                    <td>{{ $d->nama_matkul }}</td>
                                    <td>{{ $d->tahun }}</td>
                                    <td>
                                        <span class="status-badge status-{{ strtolower($d->status) }}">
                                            {{ ucfirst($d->status) }}
                                        </span>
                                    </td>
                                    <td class="td-catatan">{{ $d->catatan ?? '–' }}</td>
                                </tr>
                                @empty
                                <tr>
                                    <td colspan="5" class="td-empty">Belum ada file yang diunggah.</td>
                                </tr>
                                @endforelse
                            @else
                            <tr>
                                <td colspan="5" class="td-empty">Belum ada file yang diunggah.</td>
                            </tr>
                            @endisset
                        </tbody>
                    </table>
                </div>

            </section>

          
            {{-- VIEW 2: FORM UNGGAH FILE        --}}
            <section id="form-unggah-view" class="page-view-container hidden">

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
                        <p class="unggah-subtitle">Unggah file, dan berbagi dengan semua orang.</p>
                    </div>
                </div>

                <div class="form-unggah-card">
                    <h2 class="form-unggah-title">Unggah File</h2>

                    <form id="form-unggah" action="#" method="POST" enctype="multipart/form-data" autocomplete="off">
                        @csrf

                        {{-- Baris 1: Mata Kuliah + Tahun --}}
                        <div class="form-row-two">
                            <div class="form-group">
                                <label class="form-label">Mata Kuliah</label>
                                <div class="select-wrapper">
                                    <select name="id_matkul" class="form-select" required>
                                        <option value="" disabled selected>Pilih Mata Kuliah</option>
                                        {{-- Nanti @foreach $mataKuliah dari BE --}}
                                        <option value="1">Kalkulus I</option>
                                        <option value="2">Matematika Diskrit</option>
                                        <option value="3">Metode Numerik</option>
                                        <option value="4">Fisika</option>
                                        <option value="5">Bahasa Indonesia</option>
                                    </select>
                                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" class="select-caret">
                                        <polyline points="6 9 12 15 18 9"/>
                                    </svg>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="form-label">Tahun</label>
                                <div class="select-wrapper">
                                    <select name="tahun" class="form-select" required>
                                        <option value="" disabled selected>Pilih Tahun</option>
                                        <option value="2026">2026</option>
                                        <option value="2025">2025</option>
                                        <option value="2024">2024</option>
                                        <option value="2023">2023</option>
                                        <option value="2022">2022</option>
                                    </select>
                                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" class="select-caret">
                                        <polyline points="6 9 12 15 18 9"/>
                                    </svg>
                                </div>
                            </div>
                        </div>

                        {{-- Dosen --}}
                        <div class="form-group">
                            <label class="form-label">Dosen</label>
                            <div class="select-wrapper">
                                <select name="id_dosen" class="form-select" required>
                                    <option value="" disabled selected>Pilih Dosen</option>
                                    <option value="1">Nama Dosen 1</option>
                                    <option value="2">Nama Dosen 2</option>
                                    <option value="3">Nama Dosen 3</option>
                                </select>
                                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" class="select-caret">
                                    <polyline points="6 9 12 15 18 9"/>
                                </svg>
                            </div>
                        </div>

                        {{-- Kategori File --}}
                        <div class="form-group">
                            <label class="form-label">Kategori File</label>
                            <div class="select-wrapper">
                                <select name="kategori" class="form-select" required>
                                    <option value="" disabled selected>Pilih Jenis File (soal ujian, tugas, materi)</option>
                                    <option value="soal_ujian">Soal Ujian</option>
                                    <option value="tugas">Tugas</option>
                                    <option value="materi">Materi</option>
                                </select>
                                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" class="select-caret">
                                    <polyline points="6 9 12 15 18 9"/>
                                </svg>
                            </div>
                        </div>

                        {{-- Judul File --}}
                        <div class="form-group">
                            <label class="form-label">Judul File</label>
                            <input type="text" name="judul_file" class="form-input" placeholder="Ketik Judul File" required maxlength="200">
                        </div>

                        {{-- Dropzone --}}
                        <div class="form-group">
                            <label class="form-label">Pilih File (PDF)</label>
                            <div class="dropzone-area" id="dropzone-area">
                                <input type="file" name="file_pdf" id="file-input" accept=".pdf" style="display:none;">
                                <div class="dropzone-content" id="dropzone-content">
                                    <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
                                        <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
                                        <polyline points="14 2 14 8 20 8"/>
                                    </svg>
                                    <p>Klik atau seret file PDF di sini</p>
                                    <span>Maks. 20MB</span>
                                </div>
                                <div class="dropzone-selected hidden" id="dropzone-selected">
                                    <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
                                        <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
                                        <polyline points="14 2 14 8 20 8"/>
                                    </svg>
                                    <p id="file-name-display">nama_file.pdf</p>
                                    <button type="button" class="btn-hapus-file" id="btn-hapus-file">Hapus</button>
                                </div>
                            </div>
                            <span class="dropzone-hint">Maks. 20MB</span>
                        </div>

                        {{-- Tombol aksi --}}
                        <div class="form-unggah-actions">
                            <button type="button" class="btn-batal-unggah" id="btn-batal-unggah">Batal</button>
                            <button type="submit" class="btn-unggah-submit">Unggah</button>
                        </div>

                    </form>
                </div>

            </section>

        </main>
    </div>

</div>

<script src="{{ asset('script/unggah.js') }}"></script>
</body>
</html>