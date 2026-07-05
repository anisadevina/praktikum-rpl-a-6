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

## Instalasi dan Cara Menjalankan (Aplikasi Web)

### 1. Persiapkan Perangkat Lunak

Pastikan perangkat telah terpasang beberapa software berikut.

- **Visual Studio Code** atau editor kode lainnya.
- **Laragon** atau **XAMPP** yang telah menyediakan Apache, MySQL, PHP, dan Composer.
- **Git** untuk mengunduh source code dari GitHub.
- **Command Prompt**, **PowerShell**, atau **Terminal** untuk menjalankan perintah.
- **Composer** (apabila belum tersedia pada Laragon/XAMPP). Unduh melalui https://getcomposer.org/download/

Repository GitHub:

```text
https://github.com/anisadevina/praktikum-rpl-a-6
```

---

### 2. Menjalankan Web Server

**Laragon**

- Buka aplikasi Laragon.
- Klik **Start All**.

**XAMPP**

- Buka aplikasi XAMPP.
- Jalankan **Apache** dan **MySQL**.

---

### 3. Membuat Database

Buat sebuah database kosong bernama **study_scope**.

**Laragon**

- Klik **Database** → **Open**.
- HeidiSQL akan terbuka.
- Buat database baru dengan nama `study_scope`.

**XAMPP**

- Buka browser.
- Akses `http://localhost/phpmyadmin`.
- Pilih **New**.
- Buat database dengan nama `study_scope`.

> **Catatan:** Nama database dapat disesuaikan. Pastikan nama tersebut sama dengan nilai `DB_DATABASE` pada file `.env`.

---

### 4. Clone Repository

```bash
git clone https://github.com/anisadevina/praktikum-rpl-a-6.git
cd praktikum-rpl-a-6/src/web
```

---

### 5. Install Dependency

Install seluruh dependency Laravel menggunakan Composer.

```bash
composer install
```

---

### 6. Membuat File Environment

Salin file konfigurasi Laravel.

Windows:

```bash
copy .env.example .env
```

Linux/macOS:

```bash
cp .env.example .env
```

---

### 7. Generate Application Key

```bash
php artisan key:generate
```

---

### 8. Konfigurasi Database

Buka file `.env`, kemudian ubah bagian berikut sesuai konfigurasi MySQL yang digunakan.

```env
DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=study_scope
DB_USERNAME=root
DB_PASSWORD=
```

---

### 9. Jalankan Migrasi dan Seeder

```bash
php artisan migrate --seed
```

---

### 10. Membuat Storage Link

Perintah ini diperlukan agar dokumen yang diunggah dapat diakses melalui browser.

```bash
php artisan storage:link
```

---

### 11. Menjalankan Aplikasi

Jalankan server Laravel.

```bash
php artisan serve
```

Apabila proyek menggunakan **Vite**, jalankan juga pada terminal lain:

```bash
npm install
npm run dev
```

Setelah server berhasil berjalan, buka browser dan akses:

```text
http://127.0.0.1:8000
```
