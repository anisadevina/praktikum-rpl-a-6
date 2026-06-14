# Study Scope - Praktikum RPL

Platform akademik mahasiswa FATISDA untuk mengakses, berdiskusi, dan berbagi dokumen perkuliahan secara terpusat. 

---

## Anggota Kelompok

| Nama | NIM |
|------|-----|
| Anisa Devina Maharani | L0124002 |
| Faadhilah Hana Gustie Fatimah | L0124012 |
| Haliza Hana Maulina | L0124017 |
| Jelita Kustyara Nanda Safitri | L0124020 |

---

## Tech Stack

- **Backend:** Laravel 11 (PHP)
- **Frontend Web:** Vanilla JS + CSS (tanpa framework)
- **Database:** MySQL
- **Auth:** Laravel Session (web) & Laravel Sanctum (API mobile)

---

## Fitur yang Tersedia Saat Ini

### Mahasiswa
- **Autentikasi** - Register dan login berbasis sesi, dengan validasi NIM dan email institusi
- **Beranda** - Menampilkan mata kuliah terakhir diakses dan topik forum terbaru
- **Mata Kuliah** - Daftar semua mata kuliah dengan pencarian dan pagination
- **Detail Mata Kuliah** - Halaman detail menampilkan arsip dokumen per mata kuliah beserta tingkat kesulitan
- **Forum** - Sebuah forum untuk mengajukan pertanyaan antar pengguna
- **Unggah** - Pengguna dapat menggunggah arsip materi, tugas, dan soal

### Admin (Mahasiswa dengan akses lebih)
- **Review Dokumen** - Pengecekan unggahan pengguna sebelum dipublikasikan pada web

---

## MVP Status (3 Fitur Inti)
| No | Fitur | Kategori | Status | Screenshoot |
|:----:|:-------:|:----------:|--------|-------------|
| 1 | Autentifikasi (Login & Register) | Must Have | Selesai, berfungsi dengan baik | <img src="docs/assets/img/login(1).jpeg" width="150"> <img src="docs/assets/img/register(1).jpeg" width="150"><br><img src="docs/assets/img/register(2).jpeg" width="150"> <img src="docs/assets/img/register(3).jpeg" width="150"> |
| 2 | Beranda | Must Have | Selesai, berfungsi dengan baik | <img src="docs/assets/img/beranda(1).jpeg" width="310"><br><br><img src="docs/assets/img/beranda(2).jpeg" width="310"> |
| 3 | Menu Mata Kuliah | Must Have | Selesai, berfungsi dengan baik | <img src="docs/assets/img/mataKuliah(1).jpeg" width="310"> |

---

## Struktur Folder

```
praktikum-rpl-a-6/
├── docs/                          # Dokumentasi proyek
│   ├── uml/                       # Diagram UML (use case, activity, class)
│   ├── wireframe/                 # Link desain Figma
│   ├── srs.md                     # Software Requirements Specification
│   ├── erd.png                    # Entity Relationship Diagram
│   ├── data-dictionary.md
│   ├── user-stories.md
│   ├── backlog.md
│   └── team-contract.md
├── src/
│   ├── web/                       # Aplikasi web (Laravel)
│   └── aplikasi/                  # Aplikasi mobile Android (Kotlin, untuk project PAB)
└── tests/                         # Folder pengujian
```

---

## Instalasi (Aplikasi Web)

```bash
# 1. Clone repository
git clone <url-repo>
cd praktikum-rpl-a-6/src/web

# 2. Install dependencies
composer install

# 3. Salin file environment
cp env.example .env

# 4. Generate app key
php artisan key:generate

# 5. Konfigurasi database di .env
DB_DATABASE=fatisda_db
DB_USERNAME=root
DB_PASSWORD=

# 6. Jalankan migrasi dan seeder
php artisan migrate --seed

# 7. Buat symlink storage
php artisan storage:link

# 8. Jalankan server
php artisan serve
```

