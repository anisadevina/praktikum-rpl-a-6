<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class forumTopik extends Model
{
    protected $table = 'forum_topik';
    protected $primaryKey = 'id_topik';
    public $timestamps = false;
    protected $fillable = [
        'id_user',
        'tag',
        'pesan_topik',
        'is_anonim',
        'waktu_topik'
    ];
}
