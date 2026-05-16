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
        Schema::create('users', function (Blueprint $table) {
            $table->id('id_user');
            $table->string('nim', 20)->unique();
            $table->string('username', 100)->unique();
            $table->string('email_user', 255)->unique();
            $table->string('password', 255);
            $table->enum('role', ['user', 'admin'])->default('user');

            // Foreign Key
            $table->foreign('nim')->references('nim')->on('master_mahasiswa_fatisda');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('users');
    }
};
