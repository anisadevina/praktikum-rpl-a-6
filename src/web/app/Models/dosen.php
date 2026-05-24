<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class dosen extends Model
{
    protected $table = 'dosen';
    protected $primaryKey = 'id_dosen';
    public $timestamps = false;
    protected $fillable = ['nama_dosen', 'nuptk'];
}
