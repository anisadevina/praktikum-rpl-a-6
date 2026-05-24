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
        Schema::create('bookmark', function (Blueprint $table) {
            $table->id('id_bookmark');
            $table->unsignedBigInteger('id_user');
            $table->unsignedBigInteger('id_dokumen');

            // Foreign Keys
            $table->foreign('id_user')->references('id_user')->on('users');
            $table->foreign('id_dokumen')->references('id_dokumen')->on('dokumen');

            // UNIQUE constraint kombinasi (id_user, id_dokumen)
            $table->unique(['id_user', 'id_dokumen']);
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('bookmark');
    }
};
