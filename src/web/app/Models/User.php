<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Notifications\Notifiable;

class User extends Authenticatable
{
    use HasFactory, Notifiable;

    // 1. Beritahu Laravel bahwa Primary Key tabel ini adalah id_user (bukan id)
    protected $primaryKey = 'id_user';

    // 2. Jika di file migration kamu tidak menambahkan $table->timestamps(), 
    // matikan fitur timestamp bawaan Laravel dengan baris di bawah ini:
    public $timestamps = false;

    /**
     * 3. Daftarkan semua kolom yang bisa diisi (Mass Assignable) 
     * sesuaikan dengan struktur tabel Users buatanmu.
     */
    protected $fillable = [
        'nim',
        'username',
        'email_user',
        'password',
        'role',
    ];

    /**
     * 4. Sembunyikan password saat data user di-serialize (misal saat dijadikan API/JSON)
     */
    protected $hidden = [
        'password',
    ];

    /**
     * 5. Beritahu Laravel bahwa kolom password harus otomatis di-hash demi keamanan
     */
    protected function casts(): array
    {
        return [
            'password' => 'hashed',
        ];
    }

    /**
     * 6. Jembatan Autentikasi Laravel:
     * Karena nama kolom emailmu adalah 'email_user' (bukan 'email'), 
     * method ini wajib ada jika kamu nanti menggunakan fitur login bawaan Laravel.
     */
    public function getAuthIdentifierName()
    {
        return 'username'; // login pake username
    }
}