<!DOCTYPE html>
<html lang="id">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Detail Mata Kuliah — Study Scope</title>
  <link rel="stylesheet" href="{{ asset('style/global.css') }}" />
  <link rel="stylesheet" href="{{ asset('style/beranda.css') }}" />
  <link rel="stylesheet" href="{{ asset('style/detailMatkul.css') }}" />
</head>
<body>

<div class="app-layout">

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
          <svg viewBox="0 0 24 24">
            <rect x="3" y="3" width="7" height="7" rx="1"/>
            <rect x="14" y="3" width="7" height="7" rx="1"/>
            <rect x="14" y="14" width="7" height="7" rx="1"/>
            <rect x="3" y="14" width="7" height="7" rx="1"/>
          </svg>
          <span>Beranda</span>
        </div>
        <svg class="nav-chevron" viewBox="0 0 24 24">
          <polyline points="9 18 15 12 9 6"/>
        </svg>
      </div>

      <div class="nav-item active" data-page="matkul">
        <div class="nav-item-left">
          <svg viewBox="0 0 24 24">
            <path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"/>
            <path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"/>
          </svg>
          <span>Mata Kuliah</span>
        </div>
        <svg class="nav-chevron" viewBox="0 0 24 24">
          <polyline points="9 18 15 12 9 6"/>
        </svg>
      </div>

      <div class="nav-item" data-page="forum">
        <div class="nav-item-left">
          <svg viewBox="0 0 24 24">
            <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
          </svg>
          <span>Forum</span>
        </div>
        <svg class="nav-chevron" viewBox="0 0 24 24">
          <polyline points="9 18 15 12 9 6"/>
        </svg>
      </div>

      <div class="nav-item" data-page="unggah">
        <div class="nav-item-left">
          <svg viewBox="0 0 24 24">
            <polyline points="16 16 12 12 8 16"/>
            <line x1="12" y1="12" x2="12" y2="21"/>
            <path d="M20.39 18.39A5 5 0 0 0 18 9h-1.26A8 8 0 1 0 3 16.3"/>
          </svg>
          <span>Unggah</span>
        </div>
        <svg class="nav-chevron" viewBox="0 0 24 24">
          <polyline points="9 18 15 12 9 6"/>
        </svg>
      </div>

      <div class="nav-item" data-page="arsip">
        <div class="nav-item-left">
          <svg viewBox="0 0 24 24">
            <path d="M19 21l-7-5-7 5V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2z"/>
          </svg>
          <span>Arsip</span>
        </div>
        <svg class="nav-chevron" viewBox="0 0 24 24">
          <polyline points="9 18 15 12 9 6"/>
        </svg>
      </div>

    </nav>

    <div class="sidebar-footer">
      <div class="nav-item" id="btn-keluar">
        <div class="nav-item-left">
          <svg viewBox="0 0 24 24">
            <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/>
            <polyline points="16 17 21 12 16 7"/>
            <line x1="21" y1="12" x2="9" y2="12"/>
          </svg>
          <span>Keluar</span>
        </div>
        <svg class="nav-chevron" viewBox="0 0 24 24">
          <polyline points="9 18 15 12 9 6"/>
        </svg>
      </div>
    </div>

  </aside>

  <div class="main-content">

    <header class="topbar">
      <div class="search-bar">
        <svg viewBox="0 0 24 24">
          <circle cx="11" cy="11" r="8"/>
          <line x1="21" y1="21" x2="16.65" y2="16.65"/>
        </svg>
        <input
          type="text"
          id="search-input"
          placeholder="Cari mata kuliah"
          autocomplete="off"
        />
      </div>
      <div class="topbar-user">
        <div class="topbar-avatar">
          <svg viewBox="0 0 24 24">
            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
            <circle cx="12" cy="7" r="4"/>
          </svg>
        </div>
        <span class="topbar-username" id="topbar-username">Loading...</span>
      </div>
    </header>

    <main class="page-scroll">

      <button class="btn-back" id="btn-back">
        <svg viewBox="0 0 24 24">
          <polyline points="15 18 9 12 15 6"/>
        </svg>
       Kembali
      </button>

      <div class="breadcrumb">
        <svg viewBox="0 0 24 24">
          <path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"/>
          <path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"/>
        </svg>
        <span id="breadcrumb-text">Mata Kuliah - Loading...</span>
      </div>

      <div class="detail-hero">
        <h1 class="detail-hero-title" id="matkul-title">Loading...</h1>
        <p class="detail-hero-desc" id="matkul-desc">Memuat deskripsi mata kuliah...</p>

        <div class="detail-info-grid">

          <div class="detail-info-card">
            <div class="detail-info-label">
              <svg viewBox="0 0 24 24">
                <rect x="3" y="3" width="18" height="18" rx="2"/>
                <circle cx="8.5" cy="8.5" r="1.5"/>
                <polyline points="21 15 16 10 5 21"/>
              </svg>
              Fokus materi dari mata kuliah terkait
            </div>
            <ul class="detail-fokus-list" id="fokus-list">
              <li>Berisi materi mata kuliah <span id="fokus-matkul-name">...</span></li>
              <li>Berisi soal ujian</li>
              <li>Berisi latihan soal</li>
            </ul>
          </div>

          <div class="detail-info-card">
            <div class="detail-info-label">
              <svg viewBox="0 0 24 24">
                <rect x="3" y="3" width="18" height="18" rx="2"/>
                <circle cx="8.5" cy="8.5" r="1.5"/>
                <polyline points="21 15 16 10 5 21"/>
              </svg>
              Rating tingkat kesulitan mata kuliah
            </div>
            <div class="detail-rating-wrap">
              <span class="detail-rating-star">★</span>
              <span class="detail-rating-score" id="matkul-rating">0.0</span>
              <span class="detail-rating-max">/5.0</span>
            </div>
            <p class="detail-rating-sub" id="matkul-rating-sub">Loading...</p>
          </div>

          <div class="detail-info-card">
            <div class="detail-info-label">
              <svg viewBox="0 0 24 24">
                <rect x="3" y="3" width="18" height="18" rx="2"/>
                <circle cx="8.5" cy="8.5" r="1.5"/>
                <polyline points="21 15 16 10 5 21"/>
              </svg>
              Jumlah arsip tersedia (tugas, materi, dan soal)
            </div>
            <p class="detail-arsip-count" id="matkul-arsip-count">0</p>
            <p class="detail-arsip-sub">Arsip tersimpan</p>
          </div>

        </div>
      </div>

      <section>
        <h2 class="arsip-section-title">Arsip mata kuliah</h2>

        <div class="arsip-filter-bar">
          <span class="arsip-filter-label">Filter</span>
          <div class="arsip-filter-wrapper">
            <select class="arsip-filter-select" id="filter-tahun">
              <option value="">Semua Tahun</option>
              <option value="2024">2024</option>
              <option value="2023">2023</option>
              <option value="2022">2022</option>
              <option value="2021">2021</option>
            </select>
            <svg class="filter-chevron" viewBox="0 0 24 24">
              <polyline points="6 9 12 15 18 9"/>
            </svg>
          </div>
        </div>

        <div class="arsip-list" id="arsip-list">
           </div>
      </section>

    </main>
  </div>

</div>

<form id="logout-form" action="{{ route('logout') }}" method="POST" style="display: none;">
  @csrf
</form>

<script src="{{ asset('script/detailMatkul.js') }}"></script>
</body>
</html>