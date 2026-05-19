<?php

namespace App\Models;

use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Notifications\Notifiable;

class User extends Authenticatable
{
    use Notifiable;

    // 1. Beritahu Laravel nama tabel yang benar (Sesuai Data Dictionary)
    protected $table = 'Users';

    // 2. Beritahu Laravel bahwa Primary Key kamu BUKAN 'id', melainkan 'id_user'
    protected $primaryKey = 'id_user';

    // 3. Jika id_user adalah auto incrementing integer
    public $incrementing = true;
    protected $keyType = 'int';

    // 4. Daftarkan kolom yang boleh diisi masal
    protected $fillable = [
        'nim',
        'username',
        'email_user',
        'password',
        'role',
    ];

    protected $hidden = [
        'password',
        'remember_token',
    ];
}