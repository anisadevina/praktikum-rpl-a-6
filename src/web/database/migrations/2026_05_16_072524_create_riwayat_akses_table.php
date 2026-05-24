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
        Schema::create('riwayat_akses', function (Blueprint $table) {
            $table->id('id_riwayat_akses');
            $table->unsignedBigInteger('id_user');
            $table->unsignedBigInteger('id_matkul');
            $table->timestamp('waktu_akses')->useCurrent()->useCurrentOnUpdate();

            // Foreign Keys
            $table->foreign('id_user')->references('id_user')->on('users');
            $table->foreign('id_matkul')->references('id_matkul')->on('mata_kuliah');

            // UNIQUE constraint
            $table->unique(['id_user', 'id_matkul']);
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('riwayat_akses');
    }
};
