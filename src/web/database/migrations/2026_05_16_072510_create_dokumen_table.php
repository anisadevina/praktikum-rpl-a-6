<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration {
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('dokumen', function (Blueprint $table) {
            $table->id('id_dokumen');
            $table->unsignedBigInteger('id_user');
            $table->unsignedBigInteger('id_matkul');
            $table->unsignedBigInteger('id_dosen');
            $table->string('judul', 255);
            $table->enum('kategori_file', ['soal ujian', 'tugas', 'materi']);
            $table->year('tahun_dokumen');
            $table->string('file_path', 500);
            $table->enum('status', ['menunggu', 'disetujui', 'ditolak'])->default('menunggu');
            $table->timestamp('waktu_unggah')->useCurrent();
            $table->text('catatan_admin')->nullable();

            // Foreign Keys
            $table->foreign('id_user')->references('id_user')->on('users');
            $table->foreign('id_matkul')->references('id_matkul')->on('mata_kuliah');
            $table->foreign('id_dosen')->references('id_dosen')->on('dosen');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('dokumen');
    }
};
