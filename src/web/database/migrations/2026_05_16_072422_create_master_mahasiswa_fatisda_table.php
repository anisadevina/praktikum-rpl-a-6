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
        Schema::create('master_mahasiswa_fatisda', function (Blueprint $table) {
            $table->string('nim', 20)->primary();
            $table->string('nama', 200);
            $table->string('prodi', 100);
            $table->year('tahun_angkatan');
            $table->string('email_institusi', 255);
        });
    }

    /**
     * Reverse the migrations.
     */
    // public function down(): void
    // {
    //     Schema::dropIfExists('master_mahasiswa_fatisda');
    // }
};
