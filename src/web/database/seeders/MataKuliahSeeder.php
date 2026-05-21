<?php

namespace Database\Seeders;

use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class MataKuliahSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $matkul = [
            // Data Dummy
            // Daftar Informatika
            ['Bahasa Indonesia', 'Penggunaan bahasa Indonesia yang baik dan benar dalam karya ilmiah.', 2.50],
            ['Bahasa Inggris I', 'Dasar-dasar komunikasi bahasa Inggris dalam konteks akademik.', 2.80],
            ['Fisika', 'Prinsip dasar fisika mekanika dan elektromagnetik untuk informatika.', 3.50],
            ['Kalkulus I', 'Konsep dasar limit, turunan, dan integral satu variabel.', 4.20],
            ['Konsep Pemrograman', 'Logika dasar pemrograman dan pengenalan algoritma.', 3.80],
            ['Pendidikan Agama Islam', 'Pembentukan karakter berdasarkan nilai-nilai agama Islam.', 2.00],
            ['Pendidikan Agama Katholik', 'Pembentukan karakter berdasarkan nilai-nilai agama Katholik.', 2.00],
            ['Pendidikan Agama Kristen', 'Pembentukan karakter berdasarkan nilai-nilai agama Kristen.', 2.00],
            ['Sistem Digital', 'Logika gerbang digital, rangkaian kombinasional, dan sekuensial.', 3.70],
            ['Statistika & Probabilitas', 'Teori peluang dan pengolahan data statistik untuk penelitian.', 4.00],
            ['Aljabar Linier', 'Vektor, matriks, dan transformasi linier untuk komputasi.', 4.10],
            ['Kalkulus II', 'Integral lanjut, deret tak hingga, dan kalkulus multivariabel.', 4.50],
            ['Matematika Diskrit I', 'Logika matematika, himpunan, relasi, dan fungsi.', 3.90],
            ['Organisasi Sistem Komputer', 'Struktur internal komputer, CPU, memori, dan I/O.', 3.80],
            ['Pendidikan Kewarganegaraan', 'Wawasan kebangsaan dan hak kewajiban warga negara.', 2.00],
            ['Struktur Data & Algoritma', 'Implementasi array, stack, queue, tree, dan graph.', 4.30],
            ['Basis Data', 'Perancangan ERD, normalisasi, dan implementasi SQL.', 4.20],
            ['Desain & Analisis Algoritma', 'Analisis kompleksitas dan strategi algoritma (Greedy, DP).', 4.60],
            ['Matematika Diskrit II', 'Teori graf, pohon, dan kombinatorika lanjut.', 4.10],
            ['Metode Numerik', 'Penyelesaian masalah matematika menggunakan pendekatan numerik.', 4.30],
            ['Pemrograman Berorientasi Objek', 'Konsep class, object, inheritance, dan polymorphism.', 4.20],
            ['Pendidikan Pancasila', 'Implementasi nilai-nilai Pancasila dalam kehidupan.', 2.00],
            ['Sistem Operasi', 'Manajemen proses, memori, dan file system pada komputer.', 4.00],
            ['Jaringan Komputer', 'Protokol komunikasi, TCP/IP, subnetting, dan topologi.', 4.00],
            ['Kecerdasan Buatan', 'Dasar-dasar agen cerdas, pencarian, dan logika fuzzy.', 4.50],
            ['Pemrograman Web', 'Pengembangan website menggunakan HTML, CSS, JS, dan PHP.', 3.90],
            ['Pengembangan Aplikasi Bergerak', 'Pembuatan aplikasi mobile menggunakan Kotlin atau Flutter.', 4.20],
            ['Rekayasa Perangkat Lunak', 'Metodologi SDLC, desain sistem, dan pengujian software.', 4.50],
            ['Teori Bahasa & Automata', 'Konsep mesin turing, DFA, NFA, dan tata bahasa reguler.', 4.70],
            ['Basis Data Lanjut', 'Optimasi query, stored procedure, dan database NoSQL.', 4.10],
            ['Data Mining', 'Ekstraksi pola informasi dari sekumpulan data besar.', 4.40],
            ['Interaksi Manusia & Komputer', 'Prinsip desain antarmuka dan pengalaman pengguna (UI/UX).', 3.00],
            ['Komputasi Grid', 'Pemanfaatan sumber daya terdistribusi untuk komputasi besar.', 4.30],
            ['Kriptografi', 'Teknik keamanan data menggunakan enkripsi dan dekripsi.', 4.60],
            ['Machine Learning', 'Pengembangan model prediktif berbasis algoritma pembelajaran.', 4.70],
            ['Manajemen Jaringan', 'Administrasi perangkat jaringan dan monitoring sistem.', 3.80],
            ['Manajemen Sistem Informasi', 'Pengelolaan sumber daya informasi dalam organisasi.', 3.50],
            ['Pengolahan Citra Digital', 'Manipulasi dan ekstraksi informasi dari gambar digital.', 4.40],
            ['Pengolahan Sinyal Digital', 'Representasi dan pemrosesan sinyal dalam bentuk diskrit.', 4.50],
            ['Riset Operasi', 'Pemodelan matematis untuk pengambilan keputusan optimal.', 4.20],
            ['Sistem Terdistribusi', 'Koordinasi sistem pada beberapa mesin independen.', 4.60],
            ['Teori Game', 'Analisis strategi interaksi antar agen rasional.', 4.30],
            ['Wireless & Mobile Computing', 'Komunikasi data tanpa kabel dan komputasi mobile.', 4.00],
            ['Business Intelligence', 'Analisis data untuk mendukung strategi bisnis.', 3.70],
            ['Cyber Security', 'Perlindungan sistem dari serangan siber dan peretasan.', 4.80],
            ['Expert System', 'Sistem pakar yang meniru kemampuan pengambilan keputusan manusia.', 4.20],
            ['Jaminan Mutu Perangkat Lunak', 'Standar kualitas dan teknik testing perangkat lunak.', 3.80],
            ['Kapita Selekta Ilmu Komputer', 'Pembahasan topik terkini di bidang ilmu komputer.', 3.50],
            ['Komputasi Cloud', 'Layanan infrastruktur, platform, dan software berbasis awan.', 4.00],
            ['Metode Penelitian', 'Teknik penulisan ilmiah dan pengumpulan data penelitian.', 3.20],
            ['Natural Language Processing', 'Pemrosesan bahasa alami oleh komputer.', 4.70],
            ['Pengamanan Data Multimedia', 'Teknik perlindungan hak cipta gambar, video, dan audio.', 4.10],
            ['Proyek Perangkat Lunak', 'Implementasi proyek nyata pengembangan perangkat lunak.', 4.80],
            ['Teknik Multimedia', 'Pemrosesan elemen teks, grafik, audio, dan video.', 3.50],
            ['E-commerce', 'Model bisnis dan teknologi transaksi online.', 3.00],
            ['Etika Profesi', 'Kode etik dan tanggung jawab moral seorang IT.', 2.50],
            ['Kecerdasan Komputasional', 'Algoritma evolusioner dan jaringan syaraf tiruan.', 4.60],
            ['Kewirausahaan', 'Pengembangan jiwa startup dan bisnis mandiri.', 2.80],
            ['Semantic Web', 'Konsep web yang dapat dipahami oleh mesin.', 4.20],
            ['Teknologi IoT', 'Integrasi perangkat keras dengan internet untuk otomasi.', 4.30],

            // --- Daftar Sains Data
            ['Dasar Pemrograman', 'Pengenalan algoritma dan pemrograman dasar untuk sains data.', 3.80],
            ['Matematika Dasar', 'Fondasi matematika untuk analisis data dan statistika.', 4.00],
            ['Organisasi Dan Arsitektur Komputer', 'Struktur dan cara kerja perangkat keras komputer modern.', 3.80],
            ['Statistika Sains Data', 'Metode statistika yang difokuskan untuk analisis dataset besar.', 4.20],
            ['Aljabar Linier Untuk Sains Data', 'Penerapan matriks dan vektor dalam machine learning.', 4.50],
            ['Matematika Lanjut', 'Konsep matematika tingkat lanjut untuk pemodelan data.', 4.60],
            ['Pemodelan Statistika', 'Teknik membangun model matematis berdasarkan data observasi.', 4.40],
            ['Perancangan Dan Implementasi Basis Data', 'Desain database relasional khusus kebutuhan industri.', 4.10],
            ['Matematika Diskrit Dan Teori Graph', 'Logika diskrit dan analisis jaringan melalui graph.', 4.20],
            ['Pemodelan Dan Simulasi', 'Representasi sistem nyata ke dalam model komputasi.', 4.30],
            ['Rekayasa Sistem Informasi', 'Siklus hidup pengembangan sistem informasi enterprise.', 4.00],
            ['Sistem Manajemen Basis Data', 'Pengelolaan dan optimasi mesin database (DBMS).', 4.10],
            ['Infrastruktur Dan Platform Big Data', 'Teknologi penyimpanan dan pemrosesan data skala besar.', 4.70],
            ['Keamanan Data Dan Aplikasinya', 'Prinsip perlindungan integritas dan kerahasiaan data.', 4.60],
            ['Komputasi Terdistribusi', 'Arsitektur komputasi yang bekerja di banyak node.', 4.50],
            ['Manajemen Data Dan Infrastruktur Data Enterprise', 'Tata kelola data pada tingkat organisasi besar.', 4.20],
            ['Analisis Data Citra Biomedik', 'Pemrosesan gambar medis untuk kebutuhan diagnosis.', 4.60],
            ['Analisis Data Timeseries', 'Analisis pola data yang bergantung pada waktu.', 4.40],
            ['Basis Pengetahuan Dan Penalaran', 'Sistem yang mampu menarik kesimpulan dari data.', 4.50],
            ['Desain Aplikasi Big Data', 'Arsitektur perangkat lunak untuk menangani volume data tinggi.', 4.70],
            ['Rekayasa Dan Organisasi Sistem Big Data', 'Manajemen sistem backend untuk ekosistem big data.', 4.80],
            ['Teknologi Cloud Untuk Big Data Dan Data Analitik', 'Pemanfaatan layanan awan untuk analisis data masif.', 4.60],
        ];

        foreach ($matkul as $mk) {
            DB::table('mata_kuliah')->insert([
                'nama_matkul' => $mk[0],
                'deskripsi' => $mk[1],
                'tingkat_kesulitan' => $mk[2],
            ]);
        }
    }
}
