<!DOCTYPE html>
<html lang="id">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>{{ $dokumen['nama'] ?? 'Lihat Dokumen' }} — Study Scope</title>
  <link rel="stylesheet" href="{{ asset('style/global.css') }}" />
  <link rel="stylesheet" href="{{ asset('style/beranda.css') }}" />
  <link rel="stylesheet" href="{{ asset('style/lihatDokumen.css') }}" />
</head>
<body>

<div class="app-layout">

  <!-- SIDEBAR -->
  <aside class="sidebar">

    <div class="sidebar-logo">
      <div class="sidebar-logo-icon">
        <svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
          <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5"
                stroke="white" stroke-width="2" stroke-linecap="round"
                stroke-linejoin="round" fill="none"/>
        </svg>
      </div>
      <span class="sidebar-logo-name">Study Scope</span>
    </div>

    <nav class="sidebar-nav">

      <div class="nav-item" data-page="beranda">
        <div class="nav-item-left">
          <svg viewBox="0 0 24 24"><rect x="3" y="3" width="7" height="7" rx="1"/><rect x="14" y="3" width="7" height="7" rx="1"/><rect x="14" y="14" width="7" height="7" rx="1"/><rect x="3" y="14" width="7" height="7" rx="1"/></svg>
          <span>Beranda</span>
        </div>
        <svg class="nav-chevron" viewBox="0 0 24 24"><polyline points="9 18 15 12 9 6"/></svg>
      </div>

      <div class="nav-item" data-page="matkul">
        <div class="nav-item-left">
          <svg viewBox="0 0 24 24"><path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"/><path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"/></svg>
          <span>Mata Kuliah</span>
        </div>
        <svg class="nav-chevron" viewBox="0 0 24 24"><polyline points="9 18 15 12 9 6"/></svg>
      </div>

      <div class="nav-item" data-page="forum">
        <div class="nav-item-left">
          <svg viewBox="0 0 24 24"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/></svg>
          <span>Forum</span>
        </div>
        <svg class="nav-chevron" viewBox="0 0 24 24"><polyline points="9 18 15 12 9 6"/></svg>
      </div>

      <div class="nav-item" data-page="unggah">
        <div class="nav-item-left">
          <svg viewBox="0 0 24 24"><polyline points="16 16 12 12 8 16"/><line x1="12" y1="12" x2="12" y2="21"/><path d="M20.39 18.39A5 5 0 0 0 18 9h-1.26A8 8 0 1 0 3 16.3"/></svg>
          <span>Unggah</span>
        </div>
        <svg class="nav-chevron" viewBox="0 0 24 24"><polyline points="9 18 15 12 9 6"/></svg>
      </div>

      <div class="nav-item active" data-page="arsip">
        <div class="nav-item-left">
          <svg viewBox="0 0 24 24"><path d="M19 21l-7-5-7 5V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2z"/></svg>
          <span>Arsip</span>
        </div>
        <svg class="nav-chevron" viewBox="0 0 24 24"><polyline points="9 18 15 12 9 6"/></svg>
      </div>

    </nav>

    <div class="sidebar-footer">
      <div class="nav-item" id="btn-keluar">
        <div class="nav-item-left">
          <svg viewBox="0 0 24 24"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></svg>
          <span>Keluar</span>
        </div>
        <svg class="nav-chevron" viewBox="0 0 24 24"><polyline points="9 18 15 12 9 6"/></svg>
      </div>
    </div>

  </aside>

  <!-- MAIN -->
  <div class="main-content">

    <header class="topbar">
      <div class="search-bar">
        <svg viewBox="0 0 24 24"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>
        <input type="text" id="search-input" placeholder="Cari topik" autocomplete="off" />
      </div>
      <div class="topbar-user">
        <div class="topbar-avatar">
          <svg viewBox="0 0 24 24"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
        </div>
        <span class="topbar-username" id="topbar-username">{{ $user->username ?? 'Guest' }}</span>
      </div>
    </header>

    <main class="page-scroll">

      <!-- Dokumen Header -->
      <div class="dokumen-header">
        <div class="dokumen-header-left">
          <button class="btn-back" id="btn-back">
            <svg viewBox="0 0 24 24"><polyline points="15 18 9 12 15 6"/></svg>
            Kembali
          </button>
          <div class="dokumen-header-info">
            <h1 class="dokumen-title">
              {{-- TODO: ganti dengan $dokumen->nama_dokumen dari controller --}}
              {{ $dokumen['nama'] ?? 'Soal UAS Organisasi Sistem Komputer' }}
            </h1>
            <div class="dokumen-meta">
              <span class="dokumen-badge">
                {{-- TODO: ganti dengan $dokumen->tahun --}}
                {{ $dokumen['tahun'] ?? '2024' }}
              </span>
              <span class="dokumen-date">
                {{-- TODO: ganti dengan Carbon::parse($dokumen->waktu_unggah)->translatedFormat(...) --}}
                Diunggah pada {{ $dokumen['tanggal'] ?? '23 Mei 2026' }}
              </span>
            </div>
          </div>
        </div>

        <a class="btn-download"
           id="btn-download"
           href="{{ $dokumen['file_url'] ?? '#' }}"
           download>
          <svg viewBox="0 0 24 24"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/><polyline points="7 10 12 15 17 10"/><line x1="12" y1="15" x2="12" y2="3"/></svg>
          Unduh
        </a>
      </div>

      <!-- PDF Viewer -->
      <div class="dokumen-viewer-wrap">
        <div class="dokumen-toolbar">
          <button class="toolbar-btn" id="btn-sidebar-toggle" title="Toggle sidebar">
            <svg viewBox="0 0 24 24"><line x1="3" y1="6" x2="21" y2="6"/><line x1="3" y1="12" x2="21" y2="12"/><line x1="3" y1="18" x2="21" y2="18"/></svg>
          </button>
          <div class="toolbar-pagination">
            <span id="page-current">1</span>
            <span class="toolbar-sep">/</span>
            <span id="page-total">—</span>
          </div>
          <div class="toolbar-zoom">
            <button class="toolbar-btn" id="btn-zoom-out" title="Perkecil">
              <svg viewBox="0 0 24 24"><line x1="5" y1="12" x2="19" y2="12"/></svg>
            </button>
            <span id="zoom-level">100%</span>
            <button class="toolbar-btn" id="btn-zoom-in" title="Perbesar">
              <svg viewBox="0 0 24 24"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg>
            </button>
          </div>
          <a class="toolbar-btn toolbar-download"
             href="{{ $dokumen['file_url'] ?? '#' }}"
             download
             title="Unduh dokumen">
            <svg viewBox="0 0 24 24"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/><polyline points="7 10 12 15 17 10"/><line x1="12" y1="15" x2="12" y2="3"/></svg>
          </a>
        </div>

        <div class="dokumen-viewer" id="dokumen-viewer">
          {{-- TODO: Ganti src dengan URL file asli dari storage --}}
          {{-- Contoh: src="{{ asset('storage/' . $dokumen->file_path) }}" --}}
          {{-- Atau: src="{{ $dokumen->file_url }}" --}}
          <iframe
            id="pdf-iframe"
            src="{{ $dokumen['file_url'] ?? '' }}"
            title="Dokumen PDF"
          ></iframe>

          {{-- Placeholder tampil saat file_url kosong (dummy mode) --}}
          @if(empty($dokumen['file_url'] ?? null))
          <div class="viewer-placeholder" id="viewer-placeholder">
            <svg viewBox="0 0 24 24"><path d="M13 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V9z"/><polyline points="13 2 13 9 20 9"/></svg>
            <p>Pratinjau dokumen belum tersedia.</p>
            <span>File akan tampil di sini setelah terhubung ke storage.</span>
          </div>
          @endif
        </div>
      </div>

    </main>
  </div>

</div>

<form id="logout-form" action="{{ route('logout') }}" method="POST" style="display:none;">
  @csrf
</form>

<script src="{{ asset('script/lihatDokumen.js') }}"></script>
</body>
</html>