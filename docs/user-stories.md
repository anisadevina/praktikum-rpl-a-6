## USER STORIES

### 1. As a user, I want to mengunggah soal ujian, tugas, dan materi so that saya bisa berbagi dengan user lain.

- AC-1: Given nama sudah login, When saya membuka halaman unggah dan mengisi detail kategori (mata kuliah, tahun, dosen) lalu menekan tombol unggah, Then file berhasil tersimpan dengan status “Menunggu” dan belum muncul di halaman publik.
- AC-2: Given user sudah login dan membuka halaman Unggah, When saya memilih file dengan format selain PDF, Then sistem menampilkan peringatan bahwa format file tidak didukung dan file tidak tersimpan.

### 2. As a user I want to melihat arsip soal ujian, tugas, materi, dan insight dari berbagai mata kuliah. So that saya dapat mempelajari kembali materi kuliah tersebut secara terstruktur untuk persiapan ujian atau referensi di masa mendatang.

- AC-1: Given user sudah login dan berada di dashboard, When saya mengetik nama mata kuliah di bar pencarian dan memilih hasilnya, Then saya masuk ke halaman mata kuliah yang menampilkan insight di bagian atas dan daftar arsip soal, tugas, serta materi di bagian bawah.
- AC-2: Given user sudah login dan berada di dashboard, When saya menekan page riwayat mata kuliah yang dilihat, Then saya masuk ke halaman mata kuliah yang menampilkan insight di bagian atas dan daftar arsip soal, tugas, serta materi di bagian bawah.

### 3. As a user, I want to melihat, mengajukan, dan menjawab pertanyaan pada kolom forum, so that saya dapat menemukan solusi atas kendala yang saya alami sekaligus membantu memberikan jawaban bagi pengguna lain yang membutuhkan bantuan.

- AC-1: Given user berada di dashboard aplikasi, When user menekan menu “Forum”, Then sistem menampilkan daftar unggahan pertanyaan dan/atau jawaban dari user lain.
- AC-2: Given user berada di menu “Forum”, Then user menekan “Buat Topik”, memilih tag “General” atau “Tanya Jawab Soal”, mengisi pesan unggahan, lalu menekan tombol “Unggah”, Then sistem menyimpan topik tersebut dan menampilkannya di urutan teratas daftar unggahan forum agar bisa dilihat oleh pengguna lain.
- AC-3: Given user sedang melihat salah satu topik di forum, When user menekan tombol “Reply” dan mengetikkan solusi/jawaban dan menekan tombol “Balas”, Then sistem menampilkan jawaban tersebut tepat di bawah topik/pertanyaan terkait dan memberikan notifikasi kepada penanya (jika fitur notifikasi aktif).
- AC-4: Given user berada di formulir pengajuan pertanyaan atau kolom jawaban, When pengguna menekan tombol “Unggah” atau “Balas” tanpa menulis teks apapun, Then sistem menampilkan pesan peringatan (error) dan mencegah pengiriman data kosong ke server.

### 4. As a user, I want to bisa menyimpan/bookmark file yang menurut saya penting, so that saya dapat mengakses file yang saya butuhkan dengan mudah.

- AC-1: Given user melihat detail file soal, When user menekan tombol bookmark, Then sistem menyimpan file ke daftar bookmark pengguna.
- AC-2: Given user memilih file dari daftar bookmark, When file diklik, Then sistem menampilkan isi file tersebut.
- AC-3: Given user membuka halaman bookmark, When halaman dimuat, Then sistem menampilkan daftar file yang telah di-bookmark.

### 5. As a user, I want to mencari dan menyaring berdasarkan kategori, so that saya dapat menemukan mata kuliah yang relevan dengan cepat tanpa harus memeriksa satu per satu.

- AC-1: Given user berada pada halaman pencarian, When user mengetik kata kunci pada kolom pencarian, Then sistem akan menampilkan saran hasil pencarian secara otomatis yang relevan dengan kata kunci tersebut.
- AC-2: Given user sudah berada pada halaman hasil pencarian, when user memilih filter berdasarkan kategori tertentu, Then sistem akan memperbarui daftar tampilan secara instan dengan hanya menampilkan item yang sesuai dengan kategori terpilih.

### 6. As a user, I want to melihat dashboard utama berisi fitur pencarian, akses terakhir mata kuliah, dan forum terbaru, so that user dapat mengakses informasi penting dan melanjutkan aktivitas belajar mereka dengan lebih cepat dan efisien dalam satu halaman terpusat.

- AC-1: Given user sudah login, When dashboard utama muncul, Then user bisa melihat bar fitur pencarian serta kolom akses terakhir mata kuliah, dan forum terbaru.
- AC-2: Given saya sudah login dan belum pernah membuka mata kuliah apapun, When dashboard utama muncul, Then kolom akses terakhir menampilkan pesan kosong atau saran untuk mulai mencari mata kuliah.

### 7. As a user, I want ingin memilih antara mode tampilan cerah (light mode) dan gelap (dark mode), So that user dapat merasakan pengalaman penggunaan yang lebih personal dan menyesuaikan estetika aplikasi sesuai dengan preferensi kenyamanan saya.

- AC: Given user berada di halaman profil, When pengguna memilih salah satu opsi antara Light Mode atau Dark Mode, Then sistem akan secara otomatis mengubah tema warna aplikasi secara menyeluruh dan menyimpan preferensi tersebut agar tetap berlaku pada sesi penggunaan berikutnya.

### 8. As an admin, I want to memverifikasi unggahan user, so that saya bisa memastikan bahwa apa yang diunggah benar dan sesuai

- AC: Given admin berada di halaman detail unggahan, When admin memilih status validasi (setuju atau tolak), Then sistem akan memperbarui status unggahan tersebut di basis data dan menampilkan pesan konfirmasi keberhasilan sesuai tindakan yang dipilih.

### 9. As an admin, I want to menghapus komentar yang tidak pantas atau mengandung SARA, so that forum Q&A tetap kondusif.

- AC-1: Given admin melihat komentar yang tidak pantas atau mengandung SARA, When admin menekan tombol hapus pada komentar, Then komentar terhapus dari sistem.
- AC-2: Given komentar telah dihapus, When halaman dimuat ulang, Then komentar tersebut sudah tidak terlihat pada halaman.

### 10. As an admin, I want to hanya mahasiswa FATISDA saja yang mampu mengakses platform ini, so that forum mencangkup wilayah fatisda aja

- AC-1: Given user berada di halaman registrasi dan memasukkan username, NIM dan password mereka, When user menekan tombol “Daftar”, Then sistem melakukan pengecekan tabel master_mahasiswa_fatisda di database; jika NIM tidak ditemukan, sistem menampilkan pesan “NIM anda tidak terdaftar sebagai mahasiswa FATISDA”
- AC-2: Given user memasukkan username, NIM dan password yang valid dan terdaftar di database FATISDA, When user menekan tombol “Daftar”, Then sistem berhasil membuatkan akun dan memberikan akses penuh ke seluruh fitur platform.
- AC-3: Given NIM user sudah terdaftar di database master dan sudah memiliki akun aktif di platform, When seseorang mencoba mendaftar kembali menggunakan NIM yang sama, then sistem menampilkan pesan bahwa NIM tersebut sudah digunakan dan mengarahkan user ke halaman Login.
- AC-4: Given admin memiliki data mahasiswa baru, When admin mengunggah file (CSV/Excel) berisi NIM baru ke database, Then sistem memperbarui master_mahasiswa_fatisda sehingga mahasiswa baru tersebut bisa melakukan resgistrasi di platform.
