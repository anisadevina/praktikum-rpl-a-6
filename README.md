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
|:---:|---|:---:|---|:---:|
| 1 | Autentikasi (Login & Register) | Must Have | Fitur telah berjalan dengan baik dan telah sesuai dengan acceptance criteria. Pembatasan akses divalidasi pada halaman register, dimana hanya pengguna yang memiliki NIM tertentu yang dapat membuat akun dan masuk ke dalam website. Selain itu juga terdapat validasi tambahan yang tidak terdapat di kriteria yaitu berupa NIM dan email student harus serasi dengan yang ada di database mahasiswa FATISDA. Validasi ini sebenarnya bersifat opsional tetapi ini digunakan agar akses benar benar ditujukan kepada mahasiswa FATISDA. | <img src="docs/assets/img/login(1).jpeg" width="200"><br><em>Halaman Login tanpa input</em><br><br><img src="docs/assets/img/register(1).jpeg" width="200"><br><em>Halaman Register tanpa input</em><br><br><img src="docs/assets/img/register(2).jpeg" width="200"><br><em>Halaman Register data sudah terdaftar</em><br><br><img src="docs/assets/img/register(3).jpeg" width="200"><br><em>Halaman Register data tidak sesuai</em> |
| 2 | Beranda | Must Have | Fitur telah berjalan dengan baik dan telah sesuai dengan acceptance criteria. Di dashboard utama pengguna dapat melihat akses terakhir dari mata kuliah yang pernah dituju, forum yang terbaru, dan dapat mencari mata kuliah tertentu melalui search bar. Agar server tidak keberatan, akses terakhir yang diambil adalah 4 teratas dan forum terbaru yang diambil adalah 4 teratas. | <img src="docs/assets/img/beranda(1).jpeg" width="350"><br><em>Beranda (atas) search bar, riwayat mata kuliah</em><br><br><img src="docs/assets/img/beranda(2).jpeg" width="350"><br><em>Beranda (bawah), forum terbaru</em> |
| 3 | Menu Mata Kuliah | Must Have | Fitur telah berjalan dengan baik dan telah sesuai dengan acceptance criteria. Pengguna dapat melihat berbagai mata kuliah yang ada di FATISDA dan dapat melihat detail dari mata kuliah yang ada. List mata kuliah menggunakan pagination, agar dari sisi server tidak terlalu keberatan. Pada detail matkul, awalnya masih terdapat ketidak sesuaian tampilan pada jumlah arsip. Sehingga tim backend telah memperbaiki kesalahan tersebut. | <img src="docs/assets/img/mataKuliah(1).jpeg" width="350"><br><em>Daftar Mata Kuliah lengkap</em> |

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

