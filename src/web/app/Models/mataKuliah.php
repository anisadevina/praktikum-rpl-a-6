<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class mataKuliah extends Model
{
    protected $table = 'mata_kuliah';
    protected $primaryKey = 'id_matkul';
    public $timestamps = false;
    protected $fillable = ['nama_matkul', 'deskripsi', 'tingkat_kesulitan'];
}
