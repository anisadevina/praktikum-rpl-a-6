# Software Requirements Specification (SRS)

---

## 1. Pendahuluan

### 1.1 Tujuan Dokumen
Dokumen Software Requirements Specification (SRS) ini disusun sebagai acuan dalam pengembangan platform sistem informasi akademik yang kami kembangkan. Dokumen ini menjelaskan kebutuhan sistem secara menyeluruh agar proses pengembangan dapat berjalan terarah dan sesuai dengan tujuan yang diharapkan.

Dokumen ini bertujuan untuk:
1. Mendeskripsikan kebutuhan perangkat lunak dari sistem informasi akademik yang dikembangkan untuk mahasiswa FATISDA.
2. Menjelaskan kebutuhan fungsional sistem, termasuk fitur pengelolaan arsip soal, materi, insight pembelajaran, serta forum diskusi.
3. Menjelaskan kebutuhan non-fungsional sistem yang mencakup aspek performa, keamanan, dan kemudahan penggunaan.
4. Menjadi acuan bagi tim pengembang dalam proses perancangan, implementasi, dan pengujian sistem.
5. Menjadi referensi bagi pihak terkait dalam memahami ruang lingkup dan tujuan sistem yang digunakan.
6. Menjadi acuan dalam proses validasi untuk memastikan bahwa setiap fitur yang dikembangkan sesuai dengan user story yang telah ditentukan.

### 1.2 Ruang Lingkup Sistem
Ruang lingkup sistem ini mencakup pengembangan sebuah platform sistem informasi akademik terintegrasi yang ditujukan khusus bagi mahasiswa FATISDA. Sistem ini menyediakan fitur arsip soal ujian, tugas, materi, dan insight mata kuliah. Selain itu, terdapat fitur pendukung berupa bookmark file, pencarian, penyaringan, pengunggahan file, forum diskusi, sekaligus dashboard yang menampilkan informasi seperti akses terakhir, forum terbaru, dan lainnya.

Dalam operasionalnya, sistem melibatkan dua peran utama yaitu admin dan mahasiswa, di mana admin berfokus dalam memverifikasi unggahan, mengelola konten, serta menjaga kualitas dan ketertiban platform. Akses terhadap sistem dibatasi hanya untuk mahasiswa FATISDA melalui proses validasi data, sehingga penggunaan sistem tetap berfokus pada lingkup akademik yang relevan.

### 1.3 Definisi dan Akronim

| Istilah/Akronim | Kepanjangan | Deskripsi/Makna |
|---|---|---|
| User Story | - | Deskripsi ringkas mengenai fitur atau kebutuhan sistem yang ditulis dari perspektif Aktor/Pengguna |
| AC | Acceptance Criteria | Kriteria spesifik yang harus dipenuhi agar suatu fitur atau User Story dianggap selesai dan berfungsi dengan baik |
| Product Backlog | - | Daftar prioritas fitur dan kebutuhan sistem yang akan dikembangkan, dikategorikan berdasarkan tingkat kepentingannya *(Must-have, Should-have, Could-have, Won't-have)* |
| Admin | Administrator | Pengguna dengan hak akses khusus untuk memverifikasi unggahan, memoderasi forum, mengelola data Insight, dan memperbarui data master mahasiswa |
| Pengguna (Mahasiswa) | - | Mahasiswa FATISDA yang telah memiliki akun terverifikasi dan berhasil login ke dalam sistem untuk mengakses materi, forum, dan insight mata kuliah |
| PDF | Portable Document Format | Format berkas digital standar dan satu-satunya format yang diizinkan sistem saat pengguna (Mahasiswa) mengunggah dokumen (soal, tugas, materi) |
| Bookmark | - | Fitur yang memungkinkan pengguna (Mahasiswa) menyimpan pintasan ke suatu file agar mudah diakses kembali melalui halaman khusus |
| Forum | - | Ruang diskusi interaktif di dalam sistem tempat pengguna (Mahasiswa) dapat mengajukan pertanyaan dan memberikan jawaban terkait mata kuliah |
| Insight | - | Informasi singkat pada halaman mata kuliah yang memberikan gambaran umum, meliputi fokus materi dan tingkat kesulitan berdasarkan survei |
| FATISDA | Fakultas Teknologi Informasi dan Sains Data | Entitas fakultas yang menjadi batasan ruang lingkup target pengguna. Hanya mahasiswa terdaftar dari FATISDA yang dapat membuat akun |
| CSV | Comma Separated Value | Format dokumen teks sederhana yang digunakan Admin untuk mengunggah data massal mahasiswa baru ke database |
| G-Form | Google Form | Formulir digital pihak ketiga yang digunakan Admin untuk mengumpulkan survei tingkat kesulitan mata kuliah dari pengguna (Mahasiswa) di akhir semester |
| NIM | Nomor Induk Mahasiswa | Nomor identifikasi unik mahasiswa yang digunakan sebagai primary key dalam proses validasi saat registrasi akun baru |
| SARA | Suku, Agama, Ras, dan Antargolongan | Kategori sentimen negatif yang menjadi dasar bagi Admin untuk menghapus komentar dari forum demi menjaga kondusifitas |

---

## 2. Deskripsi Umum

### 2.1 Perspektif Produk
StudyScoope merupakan platform sistem informasi akademik berbasis web yang dikembangkan dan dirancang untuk mahasiswa di lingkungan FATISDA UNS. Platform ini dikembangkan sebagai wadah digital terpusat yang memudahkan mahasiswa dalam mengakses arsip soal ujian, materi, dan tugas, serta dilengkapi fitur ruang diskusi online antar mahasiswa.

Pada tahap awal pengembangan, cakupan StudyScoope terbatas hanya berada di lingkungan FATISDA saja. Pembatasan ini dilakukan untuk mengurangi beban kerja karena waktu pengembangan yang terbatas, tetapi platform tetap harus menyediakan data yang faktual dan relevan. Pengembangan ke skala lebih besar, seperti skala universitas, dapat dipertimbangkan jika kedepannya platform sudah teruji stabil dan valid di lingkungan FATISDA.

StudyScoope berdiri sebagai sistem mandiri yang tidak terintegrasi dengan sistem resmi akademik kampus, namun tetap menggunakan data faktual yang dapat diakses oleh publik. Akses pengguna (Mahasiswa) ke platform dibatasi hanya untuk mahasiswa FATISDA yang tervalidasi melalui NIM mahasiswa saat registrasi akun, sehingga seluruh konten dan diskusi di dalamnya tetap relevan dan terfokus pada kegiatan akademik di FATISDA.

### 2.2 Fungsi Produk
StudyScoope menyediakan enam fungsi utama yang saling terintegrasi dalam satu platform, yaitu:

1. Dashboard utama yang menampilkan pencarian mata kuliah (search bar), riwayat akses terakhir mata kuliah, dan forum diskusi terbaru.
2. Halaman mata kuliah yang terdiri dari insight mata kuliah (tingkat kesulitan dan fokus materi), arsip soal ujian, materi, dan tugas.
3. Fitur unggah file yang memungkinkan pengguna (Mahasiswa) mengunggah soal ujian, materi, dan tugas per mata kuliah dengan proses verifikasi oleh admin sebelum dipublikasikan.
4. Forum diskusi yang memungkinkan percakapan dan tanya jawab antar pengguna (Mahasiswa) dengan opsi anonim.
5. Fitur bookmark file soal ujian, materi, atau tugas per mata kuliah agar pengguna (Mahasiswa) bisa mengakses kembali dengan cepat.
6. Fitur pencarian dan filter di halaman mata kuliah berdasarkan kategori tertentu.

### 2.3 Karakteristik Pengguna
Sistem ini memiliki dua jenis pengguna utama, yaitu Mahasiswa dan Admin, dengan karakteristik sebagai berikut:

**Pengguna (Mahasiswa)**
- Merupakan mahasiswa yang menggunakan sistem untuk membantu kegiatan pembelajaran.
- Memiliki kemampuan dasar dalam menggunakan perangkat komputer dan internet.
- Dapat mengakses arsip soal, tugas, materi, dan insight pembelajaran per mata kuliah.
- Dapat melakukan pencarian mata kuliah.
- Dapat mengunggah soal ujian, tugas, dan materi untuk dibagikan kepada pengguna lain.
- Berpartisipasi aktif dalam forum diskusi dengan mengajukan pertanyaan dan memberikan jawaban.
- Memanfaatkan fitur bookmark untuk menyimpan file yang dianggap penting.
- Dapat memberikan respons terhadap survei tingkat kesulitan mata kuliah.
- Tidak memiliki akses untuk melakukan pengelolaan sistem.

**Pengguna (Admin)**
- Merupakan pengguna yang memiliki tanggung jawab dalam pengelolaan dan pengawasan sistem.
- Memiliki pemahaman yang lebih baik terkait penggunaan sistem.
- Bertugas menjaga kualitas dan kesesuaian konten yang tersedia dalam sistem.
- Melakukan pengawasan terhadap aktivitas pengguna untuk menjaga lingkungan sistem tetap kondusif.
- Mengelola insight mata kuliah, termasuk menambahkan informasi fokus materi dan tingkat kesulitan.
- Mengelola distribusi survei tingkat kesulitan mata kuliah kepada pengguna.
- Memiliki akses penuh terhadap fitur manajemen sistem.

### 2.4 Batasan
Berikut batasan sistem pada platform Study Scope:
- Hanya dapat diakses oleh mahasiswa FATISDA yang telah terverifikasi melalui data yang valid, sehingga pengguna (Mahasiswa) di luar lingkup tersebut tidak memiliki akses ke dalam sistem.
- Sistem difokuskan pada pengelolaan dan penyediaan arsip akademik seperti soal ujian, tugas, materi, serta forum diskusi, sehingga tidak mencakup fitur pembelajaran interaktif seperti rekaman kelas daring.
- File yang dapat diunggah dibatasi pada format PDF dan harus melalui proses verifikasi oleh admin sebelum dapat dipublikasi.
- Sistem hanya dioptimalkan untuk tampilan desktop dan belum mendukung penggunaan secara optimal pada perangkat mobile atau tablet.
- Data pengguna (Mahasiswa) yang digunakan dalam sistem dibatasi hingga 90 identitas sebagai bagian dari cakupan pengembangan dan pengujian sistem.
- Interaksi pada forum diskusi terbatas pada tanya jawab berbasis teks, tidak mendukung fitur komunikasi real-time atau video call.

---

## 3. Kebutuhan Fungsional

| ID | Deskripsi | Prioritas | Ref |
|---|---|---|---|
| FR-01 | Sistem menyediakan halaman dashboard utama yang menampilkan pencarian mata kuliah (search bar), riwayat akses terakhir mata kuliah, dan forum diskusi terbaru. | High | US-06 |
| FR-02 | Sistem memungkinkan pengguna (Mahasiswa) mencari mata kuliah pada kolom pencarian di dashboard dengan mengetikkan kata kunci nama mata kuliah. | High | US-05 |
| FR-03 | Sistem memungkinkan pengguna (Mahasiswa) melihat arsip soal, tugas, materi, dan insight pembelajaran berdasarkan mata kuliah yang dipilih. | High | US-02 |
| FR-04 | Sistem menyediakan halaman unggah yang memungkinkan (Mahasiswa) mengunggah soal ujian, materi, dan tugas per mata kuliah dalam format PDF, disertai pengisian detail kategori (mata kuliah, tahun, dosen), dan menampilkan pesan peringatan jika format file tidak sesuai. | High | US-01 |
| FR-05 | Sistem menampilkan halaman ruang per mata kuliah yang berisi insight (tingkat kesulitan dan fokus materi) serta arsip soal ujian, tugas, dan materi yang dapat dikategorikan berdasarkan tahun. | High | US-02 |
| FR-06 | Sistem menyediakan kolom forum tanya jawab yang memungkinkan pengguna (Mahasiswa) melihat, mengajukan, dan menjawab pertanyaan dari pengguna (Mahasiswa) lain, serta menampilkan pesan error jika pengguna (Mahasiswa) mencoba mengirim data kosong. | High | US-03 |
| FR-07 | Sistem menyediakan fitur bookmark file tugas, soal, dan materi, dan menampilkan daftar file tersebut pada satu halaman bookmark khusus. | High | US-04 |
| FR-08 | Sistem memungkinkan admin memverifikasi file yang diunggah sebelum file tersebut ditampilkan dan dapat dilihat publik. | High | US-08 |
| FR-09 | Sistem membatasi akses platform hanya untuk mahasiswa FATISDA dengan melakukan validasi NIM pada saat registrasi akun. | Medium | US-10 |
| FR-10 | Sistem memungkinkan admin mengelola insight mata kuliah dengan mengunggah informasi fokus materi dan data tingkat kesulitan berdasarkan hasil survei akhir semester. | Medium | US-11 |
| FR-11 | Sistem menampilkan pop-up pemberitahuan pengisian survei tingkat kesulitan kepada pengguna (Mahasiswa) saat admin mengunggah tautan survei di akhir semester, lalu mengarahkan pengguna (Mahasiswa) ke halaman G-Form. | Medium | US-11 |
| FR-12 | Sistem memungkinkan admin menghapus komentar pada forum diskusi yang tidak pantas atau mengandung SARA. | Medium | US-09 |
| FR-13 | Sistem memastikan komentar yang telah dihapus admin tidak ditampilkan kembali pada halaman forum setelah halaman dimuat ulang. | Medium | US-09 |
| FR-14 | Sistem memungkinkan admin mengunggah file CSV/Excel berisi data NIM mahasiswa untuk memperbarui basis data akses platform. | High | US-10 |
| FR-15 | Sistem memungkinkan pengguna (Mahasiswa) memilih mode tampilan cerah (light mode) atau gelap (dark mode) melalui halaman profil. | Low | US-07 |

---

## 4. Kebutuhan Non-Fungsional

| ID | Kategori | Deskripsi |
|---|---|---|
| NFR-01 | Performance | Sistem harus mampu memuat halaman utama (dashboard) dalam waktu kurang dari 5 detik pada kondisi jaringan normal. |
| NFR-02 | Performance | Proses pencarian data mata kuliah harus menampilkan hasil dalam waktu maksimal 5 detik setelah pengguna (Mahasiswa) memasukkan kata kunci. |
| NFR-03 | Security | Akses sistem hanya berlaku untuk mahasiswa FATISDA melalui proses verifikasi data dengan mencocokkan NIM pengguna (Mahasiswa) yang melakukan registrasi dengan database yang tersedia. |
| NFR-04 | Security | Password pengguna disimpan dalam bentuk hash menggunakan algoritma bcrypt, tidak disimpan dalam bentuk plain text. |
| NFR-05 | Usability | Sistem menampilkan pesan kesalahan (error message) dalam Bahasa Indonesia dalam waktu maksimal 5 detik setelah pengguna melakukan input yang tidak valid, seperti NIM tidak terdaftar atau format file salah. |
| NFR-06 | Usability | Sistem menampilkan indikator visual atau pesan konfirmasi sukses (success message) dalam waktu maksimal 5 detik setelah suatu tindakan berhasil dilakukan, seperti unggahan file masuk antrian validasi atau topik forum berhasil dibuat. |

---

## 5. Catatan dan Asumsi

### Asumsi
- Pengguna diasumsikan memiliki perangkat desktop dan koneksi internet yang stabil untuk mengakses sistem.
- Diasumsikan setiap pengguna memiliki identitas unik (NIM/Username dan Password) untuk masuk ke sistem.
- Admin diasumsikan memiliki satu identitas unik (Password) untuk masuk dan mengelola sistem.
- Admin diasumsikan aktif dalam memverifikasi unggahan yang masuk ke sistem.

### Catatan
- Selain mahasiswa FATISDA tidak dapat melakukan registrasi, login, maupun mengakses platform ini.
- Sistem beroperasi secara mandiri dan tidak terintegrasi langsung dengan database pusat kampus (SIAKAD), sehingga data master NIM perlu diperbarui manual oleh Admin.
- Fitur survei menggunakan G-Form membuat alur data tidak otomatis, sehingga Admin harus memasukkan rekap hasil survei secara manual ke sistem.
