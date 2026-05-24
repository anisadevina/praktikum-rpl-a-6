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
        Schema::create('forum_topik', function (Blueprint $table) {
            $table->id('id_topik');
            $table->unsignedBigInteger('id_user'); // Tipe data harus sama dengan id_user di tabel users
            $table->enum('tag', ['general', 'tanya jawab']);
            $table->text('pesan_topik');
            $table->boolean('is_anonim')->default(false);
            $table->timestamp('waktu_topik')->useCurrent();

            // Foreign Key
            $table->foreign('id_user')->references('id_user')->on('users');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('forum_topik');
    }
};
