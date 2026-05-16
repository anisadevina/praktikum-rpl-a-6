<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class dokumen extends Model
{
    protected $table = 'dokumen';
    protected $primaryKey = 'id_dokumen';
    public $timestamps = false;
    protected $fillable = [
        'id_user',
        'id_matkul',
        'id_dosen',
        'judul',
        'kategori_file',
        'tahun_dokumen',
        'file_path',
        'status',
        'waktu_unggah',
        'catatan_admin'

    ];
}
