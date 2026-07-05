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
        Schema::create('forum_balasan', function (Blueprint $table) {
            $table->id('id_forum_balasan');
            $table->unsignedBigInteger('id_topik');
            $table->unsignedBigInteger('id_user');
            $table->text('pesan_balasan');
            $table->boolean('is_anonim')->default(false);
            $table->timestamp('waktu_balasan')->useCurrent();

            // Foreign Keys
            $table->foreign('id_topik')->references('id_topik')->on('forum_topik')->onDelete('cascade');
            $table->foreign('id_user')->references('id_user')->on('users');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('forum_balasan');
    }
};
