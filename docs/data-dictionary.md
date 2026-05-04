# Data Dictionary

---

## 1. Tabel `Users`

| Tabel | Kolom | Tipe Data | Constraint | Keterangan |
|-------|-------|-----------|------------|------------|
| Users | id_user | INT | PK, AUTO_INCREMENT | ID unik pengguna |
| Users | nim | VARCHAR(20) | UNIQUE, NOT NULL | Nomor Induk Mahasiswa |
| Users | username | VARCHAR(100) | UNIQUE, NOT NULL | Username login |
| Users | email_user | VARCHAR(255) | UNIQUE, NOT NULL | Email pengguna |
| Users | password | VARCHAR(255) | NOT NULL | Password |
| Users | role | ENUM('user', 'admin') | NOT NULL, DEFAULT 'user' | Peran pengguna dalam sistem |

---

## 2. Tabel `Master_Mahasiswa_Fatisda`

| Tabel | Kolom | Tipe Data | Constraint | Keterangan |
|-------|-------|-----------|------------|------------|
| Master_Mahasiswa_Fatisda | nim | VARCHAR(20) | PK | Nomor Induk Mahasiswa |
| Master_Mahasiswa_Fatisda | nama | VARCHAR(200) | NOT NULL | Nama lengkap mahasiswa |
| Master_Mahasiswa_Fatisda | prodi | VARCHAR(100) | NOT NULL | Program studi mahasiswa |
| Master_Mahasiswa_Fatisda | tahun_angkatan | YEAR | NOT NULL | Tahun angkatan |
| Master_Mahasiswa_Fatisda | email_institusi | VARCHAR(255) | NOT NULL | Email institusi mahasiswa |

---

## 3. Tabel `Mata_Kuliah`

| Tabel | Kolom | Tipe Data | Constraint | Keterangan |
|-------|-------|-----------|------------|------------|
| Mata_Kuliah | id_matkul | INT | PK, AUTO_INCREMENT | ID unik mata kuliah |
| Mata_Kuliah | nama_matkul | VARCHAR(200) | NOT NULL | Nama mata kuliah |
| Mata_Kuliah | deskripsi | TEXT | NULL | Deskripsi mata kuliah |
| Mata_Kuliah | tingkat_kesulitan | DECIMAL(3,2) | DEFAULT 0.00 | Rating rata-rata mata kuliah |

---

## 4. Tabel `Forum_Topik`

| Tabel | Kolom | Tipe Data | Constraint | Keterangan |
|-------|-------|-----------|------------|------------|
| Forum_Topik | id_topik | INT | PK, AUTO_INCREMENT | ID unik topik forum |
| Forum_Topik | id_user | INT | FK → Users.id_user | Pengguna pembuat pertanyaan |
| Forum_Topik | tag | ENUM('general', 'tanya jawab') | NOT NULL | Kategori tag topik |
| Forum_Topik | pesan_topik | TEXT | NOT NULL | Isi konten topik |
| Forum_Topik | is_anonim | BOOLEAN | NOT NULL, DEFAULT FALSE | Opsi anonim pengguna |
| Forum_Topik | waktu_topik | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Tanggal & waktu dibuat |

---

## 5. Tabel `Dokumen`

| Tabel | Kolom | Tipe Data | Constraint | Keterangan |
|-------|-------|-----------|------------|------------|
| Dokumen | id_dokumen | INT | PK, AUTO_INCREMENT | ID unik file dokumen |
| Dokumen | id_user | INT | FK → Users.id_user | Pengguna yang mengupload |
| Dokumen | id_matkul | INT | FK → Mata_Kuliah.id_matkul | Referensi ke mata kuliah |
| Dokumen | judul | VARCHAR(255) | NOT NULL | Judul dokumen |
| Dokumen | kategori_file | ENUM('soal ujian', 'tugas', 'materi') | NOT NULL | Kategori file yang diupload pengguna |
| Dokumen | tahun_dokumen | YEAR | NOT NULL | Tahun dokumen |
| Dokumen | dosen | VARCHAR(200) | NOT NULL | Nama dosen terkait |
| Dokumen | file_path | VARCHAR(500) | NOT NULL | Lokasi penyimpanan file |
| Dokumen | status | ENUM('menunggu', 'disetujui', 'ditolak') | NOT NULL, DEFAULT 'menunggu' | Status verifikasi dokumen |
| Dokumen | waktu_unggah | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Waktu dokumen diunggah |
| Dokumen | catatan_admin | TEXT | NULL | Catatan/balasan dari admin |

---

## 6. Tabel `Bookmark`

| Tabel | Kolom | Tipe Data | Constraint | Keterangan |
|-------|-------|-----------|------------|------------|
| Bookmark | id_bookmark | INT | PK, AUTO_INCREMENT | ID unik bookmark |
| Bookmark | id_user | INT | FK → Users.id_user | Pengguna pemilik bookmark |
| Bookmark | id_dokumen | INT | FK → Dokumen.id_dokumen | File yang di-bookmark |
| Bookmark | id_user, id_dokumen | — | UNIQUE (id_user, id_dokumen) | Mencegah duplikasi penyimpanan file yang sama oleh satu pengguna |

---

## 7. Tabel `Riwayat_Akses`

| Tabel | Kolom | Tipe Data | Constraint | Keterangan |
|-------|-------|-----------|------------|------------|
| Riwayat_Akses | id_riwayat_akses | INT | PK, AUTO_INCREMENT | ID unik riwayat akses |
| Riwayat_Akses | id_user | INT | FK → Users.id_user | Pengguna yang mengakses |
| Riwayat_Akses | id_matkul | INT | FK → Mata_Kuliah.id_matkul | Mata kuliah yang diakses |
| Riwayat_Akses | waktu_akses | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Waktu akses dilakukan |
| Riwayat_Akses | id_user, id_matkul | — | UNIQUE (id_user, id_matkul) | Memastikan satu baris mewakili "akses terakhir" per mata kuliah |

---

## 8. Tabel `Forum_Balasan`

| Tabel | Kolom | Tipe Data | Constraint | Keterangan |
|-------|-------|-----------|------------|------------|
| Forum_Balasan | id_forum_balasan | INT | PK, AUTO_INCREMENT | ID unik setiap komentar yang masuk |
| Forum_Balasan | id_topik | INT | FK → Forum_Topik.id_topik ON DELETE CASCADE | Topik yang ingin dibalas oleh pengguna |
| Forum_Balasan | id_user | INT | FK → Users.id_user | Pengguna yang mengakses |
| Forum_Balasan | pesan_balasan | TEXT | NOT NULL | Isi teks jawaban |
| Forum_Balasan | is_anonim | BOOLEAN | NOT NULL, DEFAULT FALSE | Opsi anonim pengguna |
| Forum_Balasan | waktu_balasan | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Waktu balasan dibuat |