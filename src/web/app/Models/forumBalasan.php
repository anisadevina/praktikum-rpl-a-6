<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class forumBalasan extends Model
{
    protected $table = 'forum_balasan';
    protected $primaryKey = 'id_forum_balasan';
    public $timestamps = false;
    protected $fillable = ['id_topik', 'id_user', 'pesan_balasan', 'is_anonim', 'waktu_balasan'];
}
