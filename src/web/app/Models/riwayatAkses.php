<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class riwayatAkses extends Model
{
    protected $table = 'riwayat_akses';
    protected $primaryKey = 'id_riwayat_akses';
    public $timestamps = false;
    protected $fillable = ['id_user', 'id_matkul', 'waktu_akses'];
}
