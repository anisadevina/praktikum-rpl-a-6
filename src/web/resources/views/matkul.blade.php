<!DOCTYPE html>
<html lang="id">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Mata Kuliah — Study Scope</title>
  <link rel="stylesheet" href="{{ asset('style/global.css') }}" />
  <link rel="stylesheet" href="{{ asset('style/beranda.css') }}" />
  <link rel="stylesheet" href="{{ asset('style/matkul.css') }}" />
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
      <div class="nav-item" data-page="beranda">
        <div class="nav-item-left">
          <svg viewBox="0 0 24 24"><rect x="3" y="3" width="7" height="7" rx="1"/><rect x="14" y="3" width="7" height="7" rx="1"/><rect x="14" y="14" width="7" height="7" rx="1"/><rect x="3" y="14" width="7" height="7" rx="1"/></svg>
          <span>Beranda</span>
        </div>
        <svg class="nav-chevron" viewBox="0 0 24 24"><polyline points="9 18 15 12 9 6"/></svg>
      </div>
      <div class="nav-item active" data-page="matkul">
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
      <div class="nav-item" data-page="arsip">
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
        <svg viewBox="0 0 24 24"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>
        <input type="text" id="search-input" placeholder="Cari mata kuliah" autocomplete="off"/>
      </div>
      <div class="topbar-user">
        <div class="topbar-avatar">
          <svg viewBox="0 0 24 24"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
        </div>
        <span class="topbar-username" id="topbar-username">Loading...</span>
      </div>
    </header>

    <main class="page-scroll">
      
      <section class="matkul-page-header">
        <div class="matkul-header-title">
          <svg class="book-icon" viewBox="0 0 24 24">
            <path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"/>
            <path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"/>
          </svg>
          <h1>Mata Kuliah</h1>
        </div>
        <p class="matkul-header-subtitle">
          Buka dan akses seluruh arsip mata kuliah yang tersedia dengan cepat melalui kolom pencarian!!
        </p>
        <hr class="matkul-header-divider">
      </section>

      <section>
        <h2 class="section-title" id="page-title" style="display: none;">Loading...</h2>

        <h2 class="section-title" id="title-terakhir" style="display: none;">Mata Kuliah Terakhir Dilihat</h2>
        <div class="cards-grid" id="matkul-terakhir-grid" style="margin-bottom: 40px;">
           </div>

        <h2 class="section-title" id="title-semua" style="display: none;">Mata Kuliah</h2>
        <div class="cards-grid" id="matkul-semua-grid">
           </div>

        <div class="pagination-wrapper" id="pagination-container">
           </div>

      </section>
    </main>
  </div>

</div>

<form id="logout-form" action="{{ route('logout') }}" method="POST" style="display: none;">
  @csrf
</form>

<script src="{{ asset('script/matkul.js') }}"></script>
</body>
</html>