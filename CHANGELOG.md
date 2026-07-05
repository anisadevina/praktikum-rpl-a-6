# Changelog

Semua perubahan penting pada proyek ini akan didokumentasikan pada file ini.

## [1.0.0] - 2026-07-05

### Added

#### Autentikasi
- Menambahkan fitur registrasi akun mahasiswa.
- Menambahkan fitur login dan logout pengguna.
- Menambahkan validasi data registrasi dan login.
- Menambahkan autentikasi menggunakan Laravel Sanctum untuk API.

#### Beranda
- Menambahkan halaman Beranda.
- Menampilkan data utama pengguna setelah login.
- Menambahkan endpoint API untuk mengambil data beranda.

#### Mata Kuliah
- Menambahkan halaman daftar mata kuliah.
- Menambahkan halaman detail mata kuliah.
- Menampilkan arsip berdasarkan mata kuliah.
- Menambahkan endpoint API daftar dan detail mata kuliah.

#### Arsip Dokumen
- Menambahkan halaman arsip dokumen.
- Menampilkan daftar dokumen pembelajaran.
- Menambahkan fitur melihat dokumen.
- Menambahkan fitur bookmark dokumen.
- Menambahkan endpoint API arsip dan bookmark.

#### Forum Diskusi
- Menambahkan halaman forum diskusi.
- Menambahkan fitur membuat topik diskusi.
- Menambahkan fitur memberikan balasan pada topik.
- Menampilkan daftar diskusi.

#### Unggah Dokumen
- Menambahkan halaman unggah dokumen.
- Menambahkan formulir upload dokumen.
- Menambahkan validasi proses upload.

#### Review Dokumen (Admin)
- Menambahkan middleware pembatas akses admin.
- Menambahkan halaman review dokumen.
- Menambahkan halaman detail review dokumen.
- Menambahkan proses validasi/review dokumen oleh admin.

#### API
- Menambahkan REST API untuk:
  - Login
  - Register
  - Logout
  - Beranda
  - Mata Kuliah
  - Detail Mata Kuliah
  - Arsip
  - Bookmark Arsip
  - View Dokumen

#### Database
- Menambahkan model:
  - User
  - MataKuliah
  - Dokumen
  - Bookmark
  - ForumTopik
  - ForumBalasan
  - Dosen
  - RiwayatAkses
  - MasterMahasiswaFatisda

### Fixed

(Belum ada bug)
