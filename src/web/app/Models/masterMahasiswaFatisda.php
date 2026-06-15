<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class MasterMahasiswaFatisda extends Model
{
    protected $table = 'master_mahasiswa_fatisda';
    protected $primaryKey = 'nim';
    public $incrementing = false;
    protected $keyType = 'string';
    public $timestamps = false;

    protected $fillable = ['nim', 'nama', 'prodi', 'tahun_angkatan', 'email_institusi'];
}
