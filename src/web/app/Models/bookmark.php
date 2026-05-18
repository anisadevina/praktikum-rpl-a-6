<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class bookmark extends Model
{
    protected $table = 'bookmark';
    protected $primaryKey = 'id_bookmark';
    public $timestamps = false;
    protected $fillable = ['id_user', 'id_dokumen'];
}
